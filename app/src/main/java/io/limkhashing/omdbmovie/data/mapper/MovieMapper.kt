package io.limkhashing.omdbmovie.data.mapper

import io.limkhashing.omdbmovie.data.local.database.MovieEntity
import io.limkhashing.omdbmovie.data.remote.response.MovieDTO
import io.limkhashing.omdbmovie.domain.model.Movie


fun MovieDTO.toMovieEntity(
    imdbID: String?,
    title: String?,
    year: String?,
    type: Movie.MovieType?,
    poster: String?,
): MovieEntity {
    return MovieEntity(
        imdbID = imdbID ?: "",
        title = title,
        year = year,
        type = type,
        poster = poster,
    )
}

fun MovieEntity.toMovie(
    imdbID: String?,
    title: String?,
    year: String?,
    type: Movie.MovieType?,
    poster: String?,
): Movie {
    return Movie(
        imdbID = imdbID,
        title = title,
        year = year,
        type = type,
        poster = poster,
    )
}