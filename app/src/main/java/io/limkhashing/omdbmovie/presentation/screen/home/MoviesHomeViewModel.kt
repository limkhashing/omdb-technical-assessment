package io.limkhashing.omdbmovie.presentation.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.limkhashing.omdbmovie.domain.model.Movie
import io.limkhashing.omdbmovie.domain.repository.MoviesRepository
import io.limkhashing.omdbmovie.presentation.ViewState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoviesHomeViewModel @Inject constructor(
    private val moviesRepository: MoviesRepository
) : ViewModel() {

    private val _state = MutableStateFlow<ViewState<List<Movie>>>(ViewState.Idle)
    val state = _state.asStateFlow()

    val movies = mutableListOf<Movie>()

    init {
        fetchMovieList(searchKeyword = "Marvel", page = 1)
    }

    fun fetchMovieList(
        searchKeyword: String,
        type: String = Movie.MovieType.Movie.name.lowercase(),
        page: Int
    ) = viewModelScope.launch {
        val response = moviesRepository.fetchMovieList(
            searchKeyword = searchKeyword,
            type = type,
            page = page
        )
        response.collectLatest { viewState ->
            if (viewState is ViewState.Success) {
                movies.addAll(viewState.data.shuffled())
                _state.value = ViewState.Success(movies)
                return@collectLatest
            }
            _state.value = viewState
        }
    }
}