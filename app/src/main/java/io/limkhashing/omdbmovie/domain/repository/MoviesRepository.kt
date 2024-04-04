package io.limkhashing.omdbmovie.domain.repository

import io.limkhashing.omdbmovie.domain.model.Movie
import io.limkhashing.omdbmovie.presentation.ViewState
import kotlinx.coroutines.flow.Flow

interface MoviesRepository {

    suspend fun getMovieList(
        forceFetchFromRemote: Boolean,
        title: String,
        type: String = Movie.MovieType.Movie.name.lowercase(),
        page: Int
    ): Flow<ViewState<List<Movie>>>

    suspend fun getMovieDetails(imdbID: String): Flow<ViewState<Movie>>
}