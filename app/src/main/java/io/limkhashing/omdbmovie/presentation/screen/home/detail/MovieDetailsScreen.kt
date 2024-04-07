package io.limkhashing.omdbmovie.presentation.screen.home.detail

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator

data class MovieDetailsScreen(
    val imdbID: String?,
) : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current

    }
}