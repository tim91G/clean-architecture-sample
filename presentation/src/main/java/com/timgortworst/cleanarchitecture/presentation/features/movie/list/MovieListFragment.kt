package com.timgortworst.cleanarchitecture.presentation.features.movie.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.TransitionInflater
import com.timgortworst.cleanarchitecture.domain.model.movie.Movie
import com.timgortworst.cleanarchitecture.domain.model.state.Resource
import com.timgortworst.cleanarchitecture.presentation.R
import com.timgortworst.cleanarchitecture.presentation.databinding.FragmentMovieListBinding
import com.timgortworst.cleanarchitecture.presentation.extension.addSingleScrollDirectionListener
import com.timgortworst.cleanarchitecture.presentation.extension.setTranslucentStatus
import com.timgortworst.cleanarchitecture.presentation.features.movie.list.adapter.*
import com.timgortworst.cleanarchitecture.presentation.features.movie.list.decoration.NestedListMarginDecoration
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieListFragment : Fragment() {
    private val listViewModel by viewModels<MovieListViewModel>()
    private lateinit var binding: FragmentMovieListBinding

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
        binding = FragmentMovieListBinding.inflate(layoutInflater, container, false)
        sharedElementReturnTransition = TransitionInflater.from(context)
                .inflateTransition(R.transition.shared_element_transition)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition()

        setupMovieList()
        observeUI()

        binding.recyclerView.doOnPreDraw { startPostponedEnterTransition() }
        requireActivity().setTranslucentStatus(false)
    }

    private fun observeUI() {
        listViewModel.movies.observe(viewLifecycleOwner, {
            binding.noResults.visibility = View.GONE
            binding.swiperefresh.isRefreshing = false
            when (it) {
                is Resource.Error -> binding.noResults.visibility = View.VISIBLE
                Resource.Loading -> binding.swiperefresh.isRefreshing = true
                is Resource.Success -> setupAdapter(it.data)
            }
        })
    }

    private fun setupMovieList() {
//        val orientation = resources.getInteger(R.integer.gallery_orientation)
//        val padding = resources.getDimension(R.dimen.default_padding).toInt()
//        val spanLookup = MovieListSpanSizeLookup(resources, concatAdapter)
//        val gridLayoutManager = GridLayoutManager(
//            activity, spanLookup.spanSize, orientation, false
//        ).apply { spanSizeLookup = spanLookup }

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
//            addItemDecoration(GridMarginDecoration(spanLookup, padding))
            addSingleScrollDirectionListener()
        }
    }

    private fun setupAdapter(movies: List<Movie>) {
        val padding = resources.getDimension(R.dimen.default_padding).toInt()
        val itemDecoration = NestedListMarginDecoration(padding)

        val movieListAdapter = MovieListAdapter().apply {
            clickListener = { movie, view -> navigateToDetails(movie, view) }
        }
        val horizontalMovieListAdapter = NestedRecyclerAdapter(movies, movieListAdapter, itemDecoration)
        val headerAdapter = HeaderAdapter("Hello world")
        val movieFeaturedAdapter = MovieFeaturedAdapter(movies.last())

        binding.recyclerView.adapter = ConcatAdapter(
            movieFeaturedAdapter, headerAdapter, horizontalMovieListAdapter
        )
    }

    private fun navigateToDetails(movie: Movie, sharedView: View) {
        val directions =
            MovieListFragmentDirections.showMovieDetails(
                movie.title,
                movie.id,
                movie.highResImage
            )

        val extras = FragmentNavigatorExtras(sharedView to movie.highResImage)
        findNavController().navigate(directions, extras)
    }
}
