package io.limkhashing.omdbmovie.presentation.screen.home

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Icon
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import io.limkhashing.omdbmovie.domain.model.Movie
import io.limkhashing.omdbmovie.helper.Logger
import io.limkhashing.omdbmovie.presentation.ViewState
import io.limkhashing.omdbmovie.presentation.screen.profile.ProfileTab
import io.limkhashing.omdbmovie.ui.component.MovieItem
import io.limkhashing.omdbmovie.ui.theme.BackgroundColor

class MoviesHomeScreen : Screen {

    private var page = 1

    @Composable
    override fun Content() {
        val viewModel = hiltViewModel<MoviesHomeViewModel>()
        val state = viewModel.state.collectAsStateWithLifecycle().value

        MoviesHomeContent(
            state = state,
            movies = viewModel.movies,
            onPagination = {
                page++
                viewModel.fetchMovieList(searchKeyword = "Marvel", page = page)
            }
        )
    }

    @Composable
    private fun MoviesHomeContent(
        state: ViewState<List<Movie>>,
        movies: MutableList<Movie>,
        onPagination: () -> Unit,
    ) {
        val navigator = LocalNavigator.current
        val context = LocalContext.current.applicationContext

        Box(
            modifier = Modifier.fillMaxSize().fillMaxHeight(),
        ) {
            if (state.isError()) {
                val exception = state.getRequestStateException()
                Logger.logException(exception)
                Toast.makeText(context, exception?.message ?: "", Toast.LENGTH_SHORT).show()
                return@Box
            }

            if (movies.isEmpty()) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
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

                        if (index >= movies.size - 1 && !state.isLoading()) {
                            onPagination.invoke()
                        }
                    }

                    if (state.isLoading()) {
                        item {
                            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                        }
                    }
                }
            }
        }
    }


    @Composable
    private fun RowScope.TabNavigationItem(tab: Tab) {
        val navigator = LocalTabNavigator.current
        BottomNavigationItem(
            selected = navigator.current == tab,
            unselectedContentColor = LocalContentColor.current.copy(alpha = ContentAlpha.disabled),
            onClick = { navigator.current = tab },
            label = {
                Text(text = tab.options.title, fontSize = 12.sp)
            },
            icon = {
                Icon(painter = tab.options.icon ?: return@BottomNavigationItem, contentDescription = null)
            },
            alwaysShowLabel = true,
            modifier = Modifier.background(BackgroundColor)
        )
    }
}