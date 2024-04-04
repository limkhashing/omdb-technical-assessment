package io.limkhashing.omdbmovie.di.module

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.limkhashing.omdbmovie.data.remote.api.MovieApi
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Provides
    @Singleton
    fun provideMovieAPI(retrofit: Retrofit): MovieApi {
        return retrofit.create(MovieApi::class.java)
    }
}