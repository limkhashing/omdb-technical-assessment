package io.limkhashing.omdbmovie.presentation.screen.home.detail

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ImageNotSupported
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.screen.Screen
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import io.limkhashing.omdbmovie.domain.model.Movie
import io.limkhashing.omdbmovie.helper.Logger
import io.limkhashing.omdbmovie.presentation.ViewState
import io.limkhashing.omdbmovie.ui.widget.CustomLoadingDialog
import io.limkhashing.omdbmovie.ui.widget.RatingBar

data class MovieDetailsScreen(
    val imdbID: String?,
) : Screen {

    @Composable
    override fun Content() {
        val viewModel = hiltViewModel<MovieDetailsViewModel>()
        val state = viewModel.state.collectAsStateWithLifecycle().value
        LaunchedEffect(Unit) {
            viewModel.fetchMovieDetails(imdbID = imdbID)
        }
        MovieDetailsContent(
            state = state
        )
    }

    @Composable
    fun MovieDetailsContent(state: ViewState<Movie>) {
        val context = LocalContext.current.applicationContext

        state.DisplayResult(
            onLoading = {
                CustomLoadingDialog(onDismissRequest = { })
            },
            onSuccess = {
                val movie = state.getSuccessData()
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .fillMaxHeight(),
                ) {
                    val backDropImageState = rememberAsyncImagePainter(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(movie?.poster)
                            .size(Size.ORIGINAL)
                            .build()
                    ).state

                    val posterImageState = rememberAsyncImagePainter(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(movie?.poster)
                            .size(Size.ORIGINAL)
                            .build()
                    ).state

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                    ) {
                        if (backDropImageState is AsyncImagePainter.State.Error) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(220.dp)
                                    .background(MaterialTheme.colorScheme.primaryContainer),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    modifier = Modifier.size(70.dp),
                                    imageVector = Icons.Rounded.ImageNotSupported,
                                    contentDescription = movie?.title
                                )
                            }
                        }

                        if (backDropImageState is AsyncImagePainter.State.Success) {
                            Image(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(220.dp),
                                painter = backDropImageState.painter,
                                contentDescription = movie?.title,
                                contentScale = ContentScale.Crop
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .width(160.dp)
                                    .height(240.dp)
                            ) {
                                if (posterImageState is AsyncImagePainter.State.Error) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(MaterialTheme.colorScheme.primaryContainer),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            modifier = Modifier.size(70.dp),
                                            imageVector = Icons.Rounded.ImageNotSupported,
                                            contentDescription = movie?.title
                                        )
                                    }
                                }

                                if (posterImageState is AsyncImagePainter.State.Success) {
                                    Image(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .clip(RoundedCornerShape(12.dp)),
                                        painter = posterImageState.painter,
                                        contentDescription = movie?.title,
                                        contentScale = ContentScale.Crop
                                    )
                                }
                            }

                            movie?.let { movie ->
                                Column(
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        modifier = Modifier.padding(start = 16.dp),
                                        text = movie.title ?: "",
                                        fontSize = 19.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )

                                    Spacer(modifier = Modifier.height(16.dp))

                                    Row(
                                        modifier = Modifier
                                            .padding(start = 16.dp)
                                    ) {
                                        RatingBar(
                                            starsModifier = Modifier.size(18.dp),
                                            rating = movie.imdbRating?.toDouble() ?: 0.0
                                        )
                                        Text(
                                            modifier = Modifier.padding(start = 4.dp),
                                            text = movie.imdbVotes ?: "",
                                            color = Color.LightGray,
                                            fontSize = 14.sp,
                                            maxLines = 1,
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(12.dp))

                                    Text(
                                        modifier = Modifier.padding(start = 16.dp),
                                        text = "Language: " + movie.language
                                    )

                                    Spacer(modifier = Modifier.height(10.dp))

                                    Text(
                                        modifier = Modifier.padding(start = 16.dp),
                                        text = "Released " + movie.released
                                    )

                                    Spacer(modifier = Modifier.height(10.dp))
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(32.dp))

                        // Overview
                        Text(
                            modifier = Modifier.padding(start = 16.dp),
                            text = "Overview",
                            fontSize = 19.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        movie?.let {
                            Text(
                                modifier = Modifier.padding(start = 16.dp),
                                text = it.plot ?: "",
                                fontSize = 16.sp,
                            )
                        }
                        Spacer(modifier = Modifier.height(32.dp))
                    }

                }
            },
            onError = {
                val exception = state.getRequestStateException()
                Logger.logException(exception)
                Toast.makeText(context, exception?.message ?: "", Toast.LENGTH_SHORT).show()
            }
        )
    }
}