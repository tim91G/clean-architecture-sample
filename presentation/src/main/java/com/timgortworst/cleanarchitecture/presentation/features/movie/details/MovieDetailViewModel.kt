package com.timgortworst.cleanarchitecture.presentation.features.movie.details

import androidx.lifecycle.*
import com.timgortworst.cleanarchitecture.domain.model.movie.MovieDetails
import com.timgortworst.cleanarchitecture.domain.model.state.Resource
import com.timgortworst.cleanarchitecture.domain.usecase.moviedetail.GetMovieDetailsUseCase
import com.timgortworst.cleanarchitecture.domain.usecase.moviedetail.GetMovieDetailsUseCaseImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MovieDetailViewModel @Inject constructor(
    private val getMovieDetailsUseCase: GetMovieDetailsUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val movieDetails: LiveData<Resource<MovieDetails>> =
        savedStateHandle.getLiveData<Int>(STATE_ID_MOVIE).switchMap { movieId ->
            liveData {
                emit(getMovieDetailsUseCase.execute(GetMovieDetailsUseCaseImpl.Params(movieId)))
            }
        }

    fun setMovieId(movieId: Int) {
        savedStateHandle[STATE_ID_MOVIE] = movieId
    }

    companion object {
        private const val STATE_ID_MOVIE = "movieId"
    }
}