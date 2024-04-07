package io.limkhashing.omdbmovie.data.local.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import io.limkhashing.omdbmovie.domain.model.Movie

@Entity
data class MovieEntity(
    @PrimaryKey
    val imdbID: String,

    val title: String?,
    val year: String?,
    val type: Movie.MovieType?,
    val poster: String?,
    val plot: String?,

    val imdbRating: String?,
    val imdbVotes: String?,
    val language: String?,
    val released: String?,
)