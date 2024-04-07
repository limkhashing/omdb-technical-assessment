package io.limkhashing.omdbmovie.presentation.screen.home.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.limkhashing.omdbmovie.domain.model.Movie
import io.limkhashing.omdbmovie.domain.repository.MoviesRepository
import io.limkhashing.omdbmovie.presentation.ViewState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val moviesRepository: MoviesRepository,
) : ViewModel() {

    private val _state = MutableStateFlow<ViewState<Movie>>(ViewState.Idle)
    val state = _state.asStateFlow()

    fun fetchMovieDetails(imdbID: String?) = viewModelScope.launch {
        val response = moviesRepository.fetchMovieDetails(
            imdbID = imdbID
        )
        response.collect { viewState ->
            _state.value = viewState
        }
    }
}