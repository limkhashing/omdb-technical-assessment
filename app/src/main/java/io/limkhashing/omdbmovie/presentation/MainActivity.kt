package io.limkhashing.omdbmovie.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import dagger.hilt.android.AndroidEntryPoint
import io.limkhashing.omdbmovie.presentation.screen.home.MoviesHomeScreen
import io.limkhashing.omdbmovie.presentation.screen.login.LoginScreen
import io.limkhashing.omdbmovie.presentation.screen.login.LoginViewModel
import io.limkhashing.omdbmovie.ui.theme.MoviesAppTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MoviesAppTheme {
                val viewModel = hiltViewModel<LoginViewModel>()
                val startScreen = if (viewModel.getJwtSession().isNullOrBlank()) {
                    LoginScreen()
                } else {
                    MoviesHomeScreen()
                }
                Navigator(screen = startScreen) { navigator ->
                    Scaffold { innerPadding: PaddingValues ->
                        SlideTransition(
                            navigator = navigator,
                            modifier = Modifier.padding(innerPadding)
                        )
                    }
                }
            }
        }
    }
}