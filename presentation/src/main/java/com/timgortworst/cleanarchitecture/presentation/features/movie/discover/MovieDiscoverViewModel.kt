package com.timgortworst.cleanarchitecture.presentation.features.movie.discover

import androidx.lifecycle.*
import com.timgortworst.cleanarchitecture.domain.model.movie.Movie
import com.timgortworst.cleanarchitecture.domain.model.state.Resource
import com.timgortworst.cleanarchitecture.domain.usecase.movielist.GetMoviesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MovieDiscoverViewModel @Inject constructor(
   getMoviesUseCase: GetMoviesUseCase
) : ViewModel() {
    private val reloadMovies = MutableLiveData<Boolean>()
    val movies: LiveData<Resource<List<Movie>>> = Transformations.switchMap(reloadMovies) {
        getMoviesUseCase.execute().asLiveData()
    }

    init {
        reload()
    }

    fun reload() {
        reloadMovies.value = true
    }
}