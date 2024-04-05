package io.limkhashing.omdbmovie.presentation.screen.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import io.limkhashing.omdbmovie.presentation.screen.home.detail.MovieDetailsScreen

class MoviesHomeScreen : Screen {

    @Composable
    override fun Content() {
        val screenModel = hiltViewModel<MoviesHomeViewModel>()
        val navigator = LocalNavigator.current

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(text = "Home")

            Button(onClick = { navigator?.push(MovieDetailsScreen(id = 1)) }) {
                Text(text = "Go to example details")
            }
        }
    }
}