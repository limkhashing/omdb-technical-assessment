package io.limkhashing.omdbmovie.presentation.screen.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import io.limkhashing.omdbmovie.ui.component.MovieItem

class MoviesHomeScreen : Screen {

    @Composable
    override fun Content() {
        val viewModel = hiltViewModel<MoviesHomeViewModel>()
        val state = viewModel.movieListState.collectAsStateWithLifecycle().value

        MoviesHomeContent(
            state = state,
            onEvent = viewModel::onEvent
        )
    }

    @Composable
    private fun MoviesHomeContent(
        state: MovieListState,
        onEvent: (MovieListUiEvent) -> Unit
    ) {
        val navigator = LocalNavigator.current

        Box(
            modifier = Modifier.fillMaxSize().fillMaxHeight(),
        ) {
            if (state.totalMovieList.isEmpty()) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(vertical = 8.dp, horizontal = 4.dp)
                ) {
                    items(state.totalMovieList.size) { index ->
                        MovieItem(
                            movie = state.totalMovieList[index],
                            navigator = navigator
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        if (index >= state.totalMovieList.size - 1) {
                            onEvent(MovieListUiEvent.Paginate("Marvel"))
                        }
                    }
                }
            }
        }

    }
}