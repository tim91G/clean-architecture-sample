package com.timgortworst.cleanarchitecture.domain.usecase.moviediscover

import androidx.paging.*
import com.timgortworst.cleanarchitecture.domain.model.state.Resource
import com.timgortworst.cleanarchitecture.domain.repository.DiscoverRepository
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@ExperimentalPagingApi
class GetMovieDiscoverUseCaseImpl @Inject constructor(
    private val repository: DiscoverRepository
) : GetMovieDiscoverUseCase {

    override fun execute(params: Unit?) = repository.getPagedMovies().map { pagingData ->
        Resource.Success(pagingData.map { movie ->
            movie.lowResImage = "https://image.tmdb.org/t/p/w185/".plus(movie.posterPath)
            movie.highResImage = "https://image.tmdb.org/t/p/original/".plus(movie.posterPath)
            movie
        })
    }
}