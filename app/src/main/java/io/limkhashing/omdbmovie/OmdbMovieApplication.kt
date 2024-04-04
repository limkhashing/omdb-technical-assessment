package io.limkhashing.omdbmovie

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class OmdbMovieApplication: Application() {
    companion object {
        @JvmStatic
        lateinit var instance: OmdbMovieApplication
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}