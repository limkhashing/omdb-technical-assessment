package io.limkhashing.omdbmovie.presentation.screen.home

import io.limkhashing.omdbmovie.domain.model.Movie

data class MovieListState(

    val page: Int = 1,

    val totalMovieList: List<Movie> = emptyList()

)