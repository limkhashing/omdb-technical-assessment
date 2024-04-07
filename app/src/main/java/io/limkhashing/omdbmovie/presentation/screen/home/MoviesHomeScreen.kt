package io.limkhashing.omdbmovie.presentation.screen.home

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import io.limkhashing.omdbmovie.domain.model.Movie
import io.limkhashing.omdbmovie.helper.Logger
import io.limkhashing.omdbmovie.presentation.ViewState
import io.limkhashing.omdbmovie.ui.component.MovieItem
import kotlinx.coroutines.Job

class MoviesHomeScreen : Screen {

    private var page = 1

    @Composable
    override fun Content() {
        val viewModel = hiltViewModel<MoviesHomeViewModel>()
        val state by viewModel.state.collectAsStateWithLifecycle()
        val context = LocalContext.current.applicationContext

        MoviesHomeContent(
            state = state,
            totalMovieList = viewModel.totalMovieList,
            context = context,
            onPagination = {
                viewModel.fetchMovieList(searchKeyword = "Marvel", page = page)
            }
        )
    }

    @Composable
    private fun MoviesHomeContent(
        state: ViewState<List<Movie>>,
        totalMovieList: List<Movie>,
        context: Context?,
        onPagination: () -> Job
    ) {
        state.DisplayResult(
            onLoading = {
                MovieList(movies = totalMovieList, onPagination = onPagination)
            },
            onSuccess = {
                MovieList(movies = totalMovieList, onPagination = onPagination)
            },
            onError = {
                LaunchedEffect(Unit) {
                    val exception = state.getRequestStateException()
                    Logger.logException(exception)
                    Toast.makeText(context, exception?.message ?: "", Toast.LENGTH_SHORT).show()
                }
            }
        )

    }

    @Composable
    fun MovieList(movies: List<Movie>?, onPagination: () -> Job) {
        val navigator = LocalNavigator.current

        if (movies.isNullOrEmpty()) {
            CircularProgressIndicator()
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(vertical = 8.dp, horizontal = 4.dp)
            ) {
                items(movies.size) { index ->
                    MovieItem(
                        movie = movies[index],
                        navigator = navigator
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    if (index >= movies.size - 1) {
                        page += 1
                        onPagination.invoke()
                    }
                }
            }
        }
    }
}