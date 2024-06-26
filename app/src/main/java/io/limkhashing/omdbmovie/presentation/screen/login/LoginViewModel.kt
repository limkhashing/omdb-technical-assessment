package io.limkhashing.omdbmovie.presentation.screen.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.limkhashing.omdbmovie.domain.repository.SessionRepository
import io.limkhashing.omdbmovie.presentation.ViewState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val sessionRepository: SessionRepository
) : ViewModel() {

    private val _state = MutableStateFlow<ViewState<String>>(ViewState.Idle)
    val loginState = _state.asStateFlow()

    fun onUserLogin(username: String, password: String) = viewModelScope.launch {
        val response = sessionRepository.authenticateUser(username = username, password = password)
        response.collect {
            _state.value = it
        }
    }

    fun setJwtSession(jwt: String?) {
        if (jwt == null) return
        viewModelScope.launch {
            sessionRepository.setJwtSession(jwt)
        }
    }

    fun getJwtSession(): String? {
        return sessionRepository.getJwtSession()
    }
}