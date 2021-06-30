package com.timgortworst.cleanarchitecture.presentation.features.movie.discover

import androidx.lifecycle.*
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.timgortworst.cleanarchitecture.domain.model.movie.Movie
import com.timgortworst.cleanarchitecture.domain.model.state.Resource
import com.timgortworst.cleanarchitecture.domain.usecase.moviediscover.GetMovieDiscoverUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MovieDiscoverViewModel @Inject constructor(
    private val getMovieDiscoverUseCase: GetMovieDiscoverUseCase,
) : ViewModel() {

//    private val reloadMovies = MutableLiveData<Boolean>()
//    val movies: LiveData<Resource<PagingData<Movie>>> = Transformations.switchMap(reloadMovies) {
//        getMovieDiscoverUseCase.execute().asLiveData()
//    }

    fun getDiscoverFlow() = getMovieDiscoverUseCase.execute()
//
//    val flow = Pager(
//        // Configure how data is loaded by passing additional properties to
//        // PagingConfig, such as prefetchDistance.
//        PagingConfig(pageSize = 20)
//    ) {
//        ExamplePagingSource(backend, query)
//    }.flow.cachedIn(viewModelScope)
//

//    init {
//        reload()
//    }
//
//    fun reload() {
//        reloadMovies.value = true
//    }
}