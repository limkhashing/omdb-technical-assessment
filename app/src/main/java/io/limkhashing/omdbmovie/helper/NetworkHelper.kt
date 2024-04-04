package io.limkhashing.omdbmovie.helper

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import io.limkhashing.omdbmovie.OmdbMovieApplication

object NetworkHelper {
    const val AUTHORIZATION_HEADER_PROPERTY = "Authorization"

    @JvmStatic
    fun isNetworkConnected(): Boolean {
        val connectivityManager = OmdbMovieApplication.instance.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkCapabilities = connectivityManager.activeNetwork ?: return false
        try {
            val activeNetwork = connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
            return when {
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } catch (e: Exception) {
            Logger.logException(e)
            return false
        }
    }
}