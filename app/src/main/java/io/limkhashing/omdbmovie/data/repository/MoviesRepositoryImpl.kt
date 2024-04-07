package io.limkhashing.omdbmovie.data.repository

import io.limkhashing.omdbmovie.data.local.database.MovieDatabase
import io.limkhashing.omdbmovie.data.mapper.toMovie
import io.limkhashing.omdbmovie.data.mapper.toMovieEntity
import io.limkhashing.omdbmovie.data.remote.api.MovieApi
import io.limkhashing.omdbmovie.data.remote.response.SearchMoviesDTO
import io.limkhashing.omdbmovie.domain.model.Movie
import io.limkhashing.omdbmovie.domain.repository.MoviesRepository
import io.limkhashing.omdbmovie.helper.NetworkHelper
import io.limkhashing.omdbmovie.presentation.ViewState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MoviesRepositoryImpl @Inject constructor(
    private val movieApi: MovieApi,
    private val movieDatabase: MovieDatabase
) : MoviesRepository {

    override fun fetchMovieList(
        searchKeyword: String,
        type: String,
        page: Int
    ): Flow<ViewState<List<Movie>>> {
        return flow {
            emit(ViewState.Loading)

            if (NetworkHelper.isNetworkConnected()) {
                val movieListFromApi: SearchMoviesDTO = movieApi.getMoviesList(
                    searchKeyword = searchKeyword,
                    type = type,
                    page = page
                )
                val movieEntities = movieListFromApi.search?.map { movieDTO ->
                    movieDTO.toMovieEntity()
                }
                if (movieEntities.isNullOrEmpty()) {
                    throw Exception("No movies found")
                }
                movieDatabase.movieDao.insertMovieList(movieEntities)
                emit(
                    ViewState.Success(
                        movieEntities.map { movieEntity ->
                            movieEntity.toMovie()
                        }
                    )
                )
                return@flow
            }


            // If movie is already in database, return it
            val localMovieList = movieDatabase.movieDao.getMovieListLocally()
            if (localMovieList.isNotEmpty()) {
                emit(
                    ViewState.Success(
                        data = localMovieList.map { movieEntity ->
                            movieEntity.toMovie()
                        }
                    )
                )
                return@flow
            }
            throw Exception("No movies found")
        }.catch { throwable ->
            emit(ViewState.Error(exception = throwable as Exception))
        }
    }

    override fun fetchMovieDetails(imdbID: String?): Flow<ViewState<Movie>> {
        return flow {
            emit(ViewState.Loading)

            // Here we only fetch movie details from API
            val movieDTO = movieApi.getMovieDetails(imdbID = imdbID)
            val movieDetailsEntity = movieDTO?.toMovieEntity() ?: throw Exception("Movie details not found")
            movieDatabase.movieDao.insertMovieDetails(movieDetailsEntity)

            emit(ViewState.Success(movieDetailsEntity.toMovie()))
        }.catch { throwable ->
            emit(ViewState.Error(exception = throwable as Exception))
        }
    }
}