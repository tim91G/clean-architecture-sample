package com.timgortworst.cleanarchitecture.presentation.features.movie.discover.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.timgortworst.cleanarchitecture.domain.model.movie.Movie
import com.timgortworst.cleanarchitecture.presentation.R
import com.timgortworst.cleanarchitecture.presentation.databinding.MovieListItemBinding
import com.timgortworst.cleanarchitecture.presentation.features.movie.list.adapter.DiffUtilMovieItem

class MovieListAdapter :
    PagingDataAdapter<Movie, MovieListAdapter.ViewHolder>(DiffUtilMovieItem()) {
    var clickListener: ((Movie, ImageView, String) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        MovieListItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), position)
    }

    inner class ViewHolder(
        private val binding: MovieListItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: Movie?, position: Int) {
            val transName = movie?.highResImage + getItemViewType(position)

            binding.moveListItemImage.apply {
                Glide.with(context)
                    .load(movie?.lowResImage)
                    .placeholder(R.drawable.movie_placeholder)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(this)

                transitionName = transName
            }
            binding.moveListItemImage.setOnClickListener {
                movie?.let {
                    clickListener?.invoke(movie, binding.moveListItemImage, transName)
                }
            }
        }
    }
}