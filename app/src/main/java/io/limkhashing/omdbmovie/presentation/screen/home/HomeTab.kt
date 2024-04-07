package io.limkhashing.omdbmovie.presentation.screen.home

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import cafe.adriel.voyager.transitions.SlideTransition

class HomeTab(
    @Transient
    val onChangeBottomTabState : (showBottomTab : Boolean) -> Unit,
) : Tab {

    @Composable
    override fun Content() {
        Navigator(screen = MoviesHomeScreen()) { navigator ->
            LaunchedEffect(navigator.lastItem){
                onChangeBottomTabState(navigator.lastItem is MoviesHomeScreen)
            }
            SlideTransition(navigator = navigator)
        }
    }

    override val options: TabOptions
        @Composable
        get() {
            val title = "Home"
            val icon = rememberVectorPainter(Icons.Default.Home)

            return remember {
                TabOptions(
                    index = 0u,
                    title = title,
                    icon = icon
                )
            }
        }


}