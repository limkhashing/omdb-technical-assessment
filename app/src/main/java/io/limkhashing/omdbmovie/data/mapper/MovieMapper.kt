package io.limkhashing.omdbmovie.data.mapper

import io.limkhashing.omdbmovie.data.local.database.MovieEntity
import io.limkhashing.omdbmovie.data.remote.response.MovieDTO
import io.limkhashing.omdbmovie.domain.model.Movie


fun MovieDTO.toMovieEntity(): MovieEntity {
    return MovieEntity(
        imdbID = imdbID ?: "",
        title = title,
        year = year,
        type = type,
        poster = poster,
        plot = plot,
        imdbRating = imdbRating,
        imdbVotes = imdbVotes,
        language = language,
        released = released
    )
}

fun MovieEntity.toMovie(): Movie {
    return Movie(
        imdbID = imdbID,
        title = title,
        year = year,
        type = type,
        poster = poster,
        plot = plot,
        imdbRating = imdbRating,
        imdbVotes = imdbVotes,
        language = language,
        released = released
    )
}