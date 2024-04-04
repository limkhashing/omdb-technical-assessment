package io.limkhashing.omdbmovie.data.repository

import io.limkhashing.omdbmovie.core.SessionManager
import io.limkhashing.omdbmovie.domain.repository.SessionRepository
import io.limkhashing.omdbmovie.presentation.ViewState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SessionRepositoryImpl(
    private val sessionManager: SessionManager
) : SessionRepository {

    override fun authenticateUser(username: String, password: String): Flow<ViewState<String>> = flow {
        emit(ViewState.Loading)
        delay(2000) // TODO replace with actual API call, and store the JWT in SessionManager
        emit(ViewState.Success("Success"))
    }

    override fun getJwtSession(): String? {
        return sessionManager.getJwtSession()
    }

    override suspend fun setJwtSession(userJWT: String) {
        sessionManager.setJwtSession(userJWT)
    }

    override fun clearSession() {
        sessionManager.clearSession()
    }
}