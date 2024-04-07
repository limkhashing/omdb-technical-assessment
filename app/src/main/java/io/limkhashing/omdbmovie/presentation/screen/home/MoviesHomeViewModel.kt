package io.limkhashing.omdbmovie.presentation.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.limkhashing.omdbmovie.domain.model.Movie
import io.limkhashing.omdbmovie.domain.repository.MoviesRepository
import io.limkhashing.omdbmovie.presentation.ViewState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoviesHomeViewModel @Inject constructor(
    private val moviesRepository: MoviesRepository
) : ViewModel() {

    private val _state = MutableStateFlow<ViewState<List<Movie>>>(ViewState.Idle)
    val state = _state.asStateFlow()

    private var _movieListState = MutableStateFlow(MovieListState())
    val movieListState = _movieListState.asStateFlow()

    init {
        fetchMovieList(searchKeyword = "Marvel")
    }

    fun onEvent(event: MovieListUiEvent) {
        when (event) {
            MovieListUiEvent.Navigate -> {}
            is MovieListUiEvent.Paginate -> {
                fetchMovieList(searchKeyword = "Marvel")
            }
        }
    }

    fun fetchMovieList(
        searchKeyword: String,
        type: String = Movie.MovieType.Movie.name.lowercase(),
    ) = viewModelScope.launch {
        val response = moviesRepository.fetchMovieList(
            searchKeyword = searchKeyword,
            type = type,
            page = movieListState.value.page
        )
        response.collect { viewState ->
            if (viewState is ViewState.Success) {
                _movieListState.update {
                    it.copy(
                        totalMovieList = movieListState.value.totalMovieList + viewState.data.shuffled(),
                        page = movieListState.value.page + 1
                    )
                }
            }
            _state.value = viewState
        }
    }
}