package io.limkhashing.omdbmovie.data.local.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface MovieDao {
    @Upsert
    suspend fun insertMovieList(movieList: List<MovieEntity>)

    @Upsert
    suspend fun insertMovieDetails(movie: MovieEntity)

    @Query("SELECT * FROM MovieEntity WHERE imdbID = :imdbID")
    suspend fun getMovieById(imdbID: String?): MovieEntity?

    @Query("SELECT * FROM MovieEntity")
    suspend fun getMovieListLocally(): List<MovieEntity>
}