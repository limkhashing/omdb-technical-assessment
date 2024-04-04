package io.limkhashing.omdbmovie.di.module

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.limkhashing.omdbmovie.core.SessionManager
import io.limkhashing.omdbmovie.data.local.database.MovieDatabase
import io.limkhashing.omdbmovie.data.remote.api.MovieApi
import io.limkhashing.omdbmovie.data.repository.MoviesRepositoryImpl
import io.limkhashing.omdbmovie.data.repository.SessionRepositoryImpl
import io.limkhashing.omdbmovie.domain.repository.MoviesRepository
import io.limkhashing.omdbmovie.domain.repository.SessionRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideLoginRepository(
        sessionManager: SessionManager
    ): SessionRepository {
        return SessionRepositoryImpl(sessionManager)
    }

    @Provides
    @Singleton
    fun provideMoviesRepository(
        movieApi: MovieApi,
        movieDatabase: MovieDatabase
    ): MoviesRepository {
        return MoviesRepositoryImpl(movieApi = movieApi, movieDatabase = movieDatabase)
    }
}