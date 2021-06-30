package com.timgortworst.cleanarchitecture.domain.repository

import androidx.paging.PagingData
import com.timgortworst.cleanarchitecture.domain.model.movie.Movie
import kotlinx.coroutines.flow.Flow

interface DiscoverRepository {
    fun getPagedMovies(): Flow<PagingData<Movie>>
}
