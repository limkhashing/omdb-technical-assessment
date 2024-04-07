package io.limkhashing.omdbmovie.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import cafe.adriel.voyager.transitions.SlideTransition
import dagger.hilt.android.AndroidEntryPoint
import io.limkhashing.omdbmovie.presentation.screen.home.HomeTab
import io.limkhashing.omdbmovie.presentation.screen.home.MoviesHomeScreen
import io.limkhashing.omdbmovie.presentation.screen.login.LoginScreen
import io.limkhashing.omdbmovie.presentation.screen.login.LoginViewModel
import io.limkhashing.omdbmovie.presentation.screen.profile.ProfileTab
import io.limkhashing.omdbmovie.ui.theme.BackgroundColor
import io.limkhashing.omdbmovie.ui.theme.MoviesAppTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MoviesAppTheme {
                val viewModel = hiltViewModel<LoginViewModel>()
                if (viewModel.getJwtSession().isNullOrBlank()) {
                    Navigator(screen =  LoginScreen()) { navigator ->
                        Scaffold { innerPadding: PaddingValues ->
                            SlideTransition(
                                navigator = navigator,
                                modifier = Modifier.padding(innerPadding)
                            )
                        }
                    }
                } else {
                    TabNavigator(HomeTab) {
                        Scaffold(
                            content = {
                                CurrentTab()
                            },
                            bottomBar = {
                                BottomNavigation {
                                    TabNavigationItem(HomeTab)
                                    TabNavigationItem(ProfileTab)
                                }
                            }
                        )
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