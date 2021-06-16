package com.timgortworst.cleanarchitecture.presentation.features.movie.discover

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.transition.TransitionInflater
import com.timgortworst.cleanarchitecture.domain.model.movie.Movie
import com.timgortworst.cleanarchitecture.domain.model.state.Resource
import com.timgortworst.cleanarchitecture.presentation.R
import com.timgortworst.cleanarchitecture.presentation.databinding.FragmentMovieDiscoverBinding
import com.timgortworst.cleanarchitecture.presentation.extension.addSingleScrollDirectionListener
import com.timgortworst.cleanarchitecture.presentation.extension.setTranslucentStatus
import com.timgortworst.cleanarchitecture.presentation.extension.snackbar
import com.timgortworst.cleanarchitecture.presentation.features.movie.discover.adapter.MovieListAdapter
import com.timgortworst.cleanarchitecture.presentation.features.movie.discover.decoration.ItemMarginDecoration
import com.timgortworst.cleanarchitecture.presentation.features.movie.list.decoration.MovieListSpanSizeLookup.Companion.TOTAL_COLUMNS_GRID
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieDiscoverFragment : Fragment() {
    private val viewModel by viewModels<MovieDiscoverViewModel>()
    private lateinit var binding: FragmentMovieDiscoverBinding
    private val movieDiscoverAdapter by lazy {
        MovieListAdapter().apply {
            clickListener = { movie, view, transitionName ->
                navigateToDetails(movie, view, transitionName)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val inflater = TransitionInflater.from(requireContext())
        exitTransition = inflater.inflateTransition(android.R.transition.fade)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMovieDiscoverBinding.inflate(layoutInflater, container, false)
        sharedElementReturnTransition = TransitionInflater.from(context)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition()

        setupDiscoverList()
        observeUI()

        binding.recyclerView.doOnPreDraw {
            startPostponedEnterTransition()
        }
        requireActivity().setTranslucentStatus(false)

        binding.swiperefresh.setOnRefreshListener {
            viewModel.reload()
        }
    }

    private fun observeUI() {
        viewModel.movies.observe(viewLifecycleOwner, {
            binding.noResults.visibility = View.GONE
            binding.swiperefresh.isRefreshing = false
            when (it) {
                is Resource.Error -> {
                    it.errorEntity?.message?.let { msg -> view?.snackbar(msg) }
                    binding.noResults.visibility = View.VISIBLE
                }
                Resource.Loading -> binding.swiperefresh.isRefreshing = true
                is Resource.Success -> movieDiscoverAdapter.submitList(it.data)
            }
        })
    }

    private fun setupDiscoverList() {
        binding.recyclerView.apply {
            layoutManager = GridLayoutManager(activity, resources.getInteger(R.integer.gallery_columns))
            adapter = movieDiscoverAdapter
            addItemDecoration(ItemMarginDecoration(resources))
            addSingleScrollDirectionListener()
        }
    }

    private fun navigateToDetails(movie: Movie, sharedView: View, transitionName: String) {
        val directions =
            MovieDiscoverFragmentDirections.showMovieDetails(
                movie.title,
                movie.id,
                movie.highResImage,
                transitionName,
            )

        val extras = FragmentNavigatorExtras(sharedView to transitionName)
        findNavController().navigate(directions, extras)
    }
}
