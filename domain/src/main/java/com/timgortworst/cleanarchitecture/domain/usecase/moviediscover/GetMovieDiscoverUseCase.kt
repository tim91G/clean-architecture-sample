package com.timgortworst.cleanarchitecture.domain.usecase.moviediscover

import androidx.paging.PagingData
import com.timgortworst.cleanarchitecture.domain.model.movie.Movie
import com.timgortworst.cleanarchitecture.domain.usecase.FlowUseCase

interface GetMovieDiscoverUseCase : FlowUseCase<Unit, PagingData<Movie>>