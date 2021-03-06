package com.timgortworst.cleanarchitecture.presentation.di

import com.timgortworst.cleanarchitecture.domain.usecase.moviedetail.GetMovieDetailsUseCase
import com.timgortworst.cleanarchitecture.domain.usecase.moviedetail.GetMovieDetailsUseCaseImpl
import com.timgortworst.cleanarchitecture.domain.usecase.movielist.GetMoviesUseCase
import com.timgortworst.cleanarchitecture.domain.usecase.movielist.GetMoviesUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
abstract class UseCaseModule {

    @Binds
    @ViewModelScoped
    abstract fun provideGetMoviesUseCase(
        getMoviesUseCaseImpl: GetMoviesUseCaseImpl
    ): GetMoviesUseCase

    @Binds
    @ViewModelScoped
    abstract fun provideGetMovieDetailsUseCase(
        getMovieDetailsUseCaseImpl: GetMovieDetailsUseCaseImpl
    ): GetMovieDetailsUseCase
}