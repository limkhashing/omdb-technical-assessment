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
                    movieDTO.toMovieEntity(
                        imdbID = movieDTO.imdbID,
                        title = movieDTO.title,
                        year = movieDTO.year,
                        type = movieDTO.type,
                        poster = movieDTO.poster,
                    )
                }
                if (movieEntities.isNullOrEmpty()) {
                    throw Exception("No movies found")
                }
                movieDatabase.movieDao.insertMovieList(movieEntities)
                emit(
                    ViewState.Success(
                        movieEntities.map {
                            it.toMovie(
                                imdbID = it.imdbID,
                                title = it.title,
                                year = it.year,
                                type = it.type,
                                poster = it.poster,
                            )
                        }
                    )
                )
                return@flow
            }


            // If movie is already in database, return it
            val localMovieList = movieDatabase.movieDao.getMovieListByTitle(searchKeyword)
            val shouldLoadLocalMovie = localMovieList.isNotEmpty()
            if (shouldLoadLocalMovie) {
                emit(
                    ViewState.Success(
                        data = localMovieList.map { movieEntity ->
                            movieEntity.toMovie(
                                imdbID = movieEntity.imdbID,
                                title = movieEntity.title,
                                year = movieEntity.year,
                                type = movieEntity.type,
                                poster = movieEntity.poster,
                            )
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

    override fun fetchMovieDetails(imdbID: String): Flow<ViewState<Movie>> {
        return flow {
            emit(ViewState.Loading)

            if (NetworkHelper.isNetworkConnected()) {
                val movieDTO = movieApi.getMovieDetails(imdbID = imdbID)
                val movieDetailsEntity = movieDTO?.toMovieEntity(
                    imdbID = movieDTO.imdbID,
                    title = movieDTO.title,
                    year = movieDTO.year,
                    type = movieDTO.type,
                    poster = movieDTO.poster,
                ) ?: throw Exception("Movie details not found")

                // If movie is not in database, fetch it from API
                movieDatabase.movieDao.insertMovieDetails(movieDetailsEntity)
                return@flow
            }

            // If movie is already in database, return it
            val movieEntity = movieDatabase.movieDao.getMovieById(imdbID) ?: throw Exception("Movie details not found")
            emit(
                ViewState.Success(
                    movieEntity.toMovie(
                        imdbID = movieEntity.imdbID,
                        title = movieEntity.title,
                        year = movieEntity.year,
                        type = movieEntity.type,
                        poster = movieEntity.poster,
                    )
                )
            )
        }.catch { throwable ->
            emit(ViewState.Error(exception = throwable as Exception))
        }
    }
}