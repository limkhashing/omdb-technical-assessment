package io.limkhashing.omdbmovie.domain.model

import com.google.gson.annotations.SerializedName
import io.limkhashing.omdbmovie.data.remote.response.MovieDTO

// - Title
// - Year
// - imdbID
// - Type
// - Poster
data class Movie(
    val imdbID: String?,
    val title: String?,
    val year: String?,
    val type: MovieType?,
    val poster: String?
) {
    enum class MovieType {
        @SerializedName("movie")
        Movie,

        @SerializedName("series")
        Series,

        @SerializedName("episode")
        Episode
    }
}