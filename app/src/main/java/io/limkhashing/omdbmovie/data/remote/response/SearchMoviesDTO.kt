package io.limkhashing.omdbmovie.data.remote.response
import com.google.gson.annotations.SerializedName


// Search will return a list of movies with the following fields:
// - Title
// - Year
// - imdbID
// - Type
// - Poster
data class SearchMoviesDTO(
    @SerializedName("Search")
    val search: List<MovieDTO>?,
)