package io.limkhashing.omdbmovie.data.remote.api

import io.limkhashing.omdbmovie.data.remote.response.MovieDTO
import io.limkhashing.omdbmovie.data.remote.response.SearchMoviesDTO
import io.limkhashing.omdbmovie.domain.model.Movie
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieApi {

    companion object {
        const val BASE_URL = "https://www.omdbapi.com"
        const val API_KEY = "6fc87060" // TODO replace with your own API key with
    }

    @GET("/")
    suspend fun getMoviesList(
        @Query("s") searchKeyword: String,
        @Query("type") type: String = Movie.MovieType.Movie.name.lowercase(),
        @Query("page") page: Int,
        @Query("apiKey") apiKey: String = API_KEY
    ): SearchMoviesDTO

    @GET("/")
    suspend fun getMovieDetails(
        @Query("i") imdbID: String,
        @Query("apiKey") apiKey: String = API_KEY
    ): MovieDTO?
}