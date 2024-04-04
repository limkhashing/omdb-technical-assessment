package io.limkhashing.omdbmovie.helper

import android.util.Log
import io.limkhashing.omdbmovie.BuildConfig

object Logger {
    private val TAG = Logger::class.java.simpleName
    private val DEBUG = !isReleaseMode()

    // check if app is in debug or release mode
    fun isReleaseMode(): Boolean {
        return BuildConfig.BUILD_TYPE == "release"
    }

    fun log(message: String?) {
        when {
            DEBUG -> Log.d(TAG, message ?: "null")
            else -> {
                // Log to Firebase Crashlytics if Google Mobile Services is available
            }
        }
    }

    @JvmStatic
    fun logException(throwable: Throwable?) {
        if (throwable == null) return
        if (DEBUG) {
            Log.e(TAG, throwable.message, throwable)
            return
        }
        // TODO Log to Firebase Crashlytics if Google Mobile Services is available
    }
}