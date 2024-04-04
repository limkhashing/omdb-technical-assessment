package io.limkhashing.omdbmovie.di.module

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.limkhashing.omdbmovie.data.remote.api.MovieApi
import io.limkhashing.omdbmovie.network.RequestHeaderInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient, ): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .baseUrl(MovieApi.BASE_URL)
            .build()
    }

    @Provides
    @Singleton
    fun providesOkHttpClient(
        @ApplicationContext context: Context,
        requestHeaderInterceptor: RequestHeaderInterceptor,
    ): OkHttpClient = OkHttpClient.Builder()
        .addNetworkInterceptor(ChuckerInterceptor.Builder(context).build())
        .addNetworkInterceptor(requestHeaderInterceptor.jwtSessionInterceptor)
        .build()
}