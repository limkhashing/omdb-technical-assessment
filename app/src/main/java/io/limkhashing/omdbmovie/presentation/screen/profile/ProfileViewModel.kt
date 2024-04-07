package io.limkhashing.omdbmovie.presentation.screen.profile

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
class ProfileViewModel @Inject constructor(
    private val sessionRepository: SessionRepository
) : ViewModel() {

    fun clearSession() {
        sessionRepository.clearSession()
    }
}