package io.limkhashing.omdbmovie.data.repository

import io.limkhashing.omdbmovie.data.local.database.MovieDatabase
import io.limkhashing.omdbmovie.data.mapper.toMovie
import io.limkhashing.omdbmovie.data.mapper.toMovieEntity
import io.limkhashing.omdbmovie.data.remote.api.MovieApi
import io.limkhashing.omdbmovie.data.remote.response.SearchMoviesDTO
import io.limkhashing.omdbmovie.domain.model.Movie
import io.limkhashing.omdbmovie.domain.repository.MoviesRepository
import io.limkhashing.omdbmovie.presentation.ViewState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MoviesRepositoryImpl @Inject constructor(
    private val movieApi: MovieApi,
    private val movieDatabase: MovieDatabase
) : MoviesRepository {

    override suspend fun getMovieList(
        forceFetchFromRemote: Boolean,
        title: String,
        type: String,
        page: Int
    ): Flow<ViewState<List<Movie>>> {
        return flow {
            emit(ViewState.Loading)

            val localMovieList = movieDatabase.movieDao.getMovieListByTitle(title)
            val shouldLoadLocalMovie = localMovieList.isNotEmpty() && !forceFetchFromRemote

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
            
            val movieListFromApi: SearchMoviesDTO = try {
                movieApi.getMoviesList(
                    searchKeyword = title,
                    type = type,
                    page = page
                )
            } catch (e: Exception) {
                emit(ViewState.Error(exception = e))
                return@flow
            }
            

            val movieEntities = movieListFromApi.search?.map { movieDto ->
                movieDto.toMovieEntity(
                    imdbID = movieDto.imdbID,
                    title = movieDto.title,
                    year = movieDto.year,
                    type = movieDto.type,
                    poster = movieDto.poster,
                )
            }
            
            if (movieEntities.isNullOrEmpty()) {
                emit(ViewState.Error(Exception("No movies found")))
                return@flow
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
        }
    }

    override suspend fun getMovieDetails(imdbID: String): Flow<ViewState<Movie>> {
        return flow {
            emit(ViewState.Loading)

            val movieEntity = movieDatabase.movieDao.getMovieById(imdbID)
            if (movieEntity == null) {
                emit(ViewState.Error(Exception("Movie not found")))
                return@flow
            }
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
        }
    }
}