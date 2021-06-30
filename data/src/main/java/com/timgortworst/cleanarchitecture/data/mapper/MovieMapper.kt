package com.timgortworst.cleanarchitecture.data.mapper

import com.timgortworst.cleanarchitecture.data.model.DbMovie
import com.timgortworst.cleanarchitecture.data.model.NetworkMovies
import com.timgortworst.cleanarchitecture.domain.model.movie.Movie

fun DbMovie.asDomainModel(): Movie = with(this) {
    Movie(
        adult,
        backdropPath,
        id,
        originalLanguage,
        originalTitle,
        overview,
        popularity,
        posterPath,
        releaseDate,
        title,
        video,
        voteAverage,
        voteCount
    )
}

fun NetworkMovies.asDatabaseModel() : List<DbMovie> = with(this) {
    results.map {
        DbMovie(
            it.id,
            it.adult,
            it.backdropPath,
            it.originalLanguage,
            it.originalTitle,
            it.overview,
            it.popularity,
            it.posterPath,
            it.releaseDate,
            it.title,
            it.video,
            it.voteAverage,
            it.voteCount,
            page,
        )
    }
}

fun NetworkMovies.asDomainModel() : List<Movie> = with(this) {
    results.map {
        Movie(
            it.adult,
            it.backdropPath,
            it.id,
            it.originalLanguage,
            it.originalTitle,
            it.overview,
            it.popularity,
            it.posterPath,
            it.releaseDate,
            it.title,
            it.video,
            it.voteAverage,
            it.voteCount,
            "https://image.tmdb.org/t/p/w185/".plus(it.posterPath),
            "https://image.tmdb.org/t/p/original/".plus(it.posterPath),
        )
    }
}