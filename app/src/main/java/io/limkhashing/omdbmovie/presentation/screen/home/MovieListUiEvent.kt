package io.limkhashing.omdbmovie.presentation.screen.home


sealed interface MovieListUiEvent {
    data class Paginate(val category: String) : MovieListUiEvent
    data object Navigate : MovieListUiEvent
}