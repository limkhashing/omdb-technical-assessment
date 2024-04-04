package io.limkhashing.omdbmovie.network

import io.limkhashing.omdbmovie.core.SessionManager
import io.limkhashing.omdbmovie.helper.NetworkHelper
import okhttp3.Interceptor
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RequestHeaderInterceptor @Inject constructor(
    private val sessionManager: SessionManager
) {
    val jwtSessionInterceptor: Interceptor
        get() = Interceptor { chain: Interceptor.Chain ->
            val jwtSession =
                sessionManager.getJwtSession() ?: return@Interceptor chain.proceed(chain.request())
            val requestBuilder = chain.request().newBuilder()
            requestBuilder.addHeader(NetworkHelper.AUTHORIZATION_HEADER_PROPERTY, jwtSession)
            chain.proceed(requestBuilder.build())
        }
}