package io.limkhashing.omdbmovie.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ImageNotSupported
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toBitmap
import cafe.adriel.voyager.navigator.Navigator
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import io.limkhashing.omdbmovie.domain.model.Movie
import io.limkhashing.omdbmovie.presentation.screen.home.detail.MovieDetailsScreen
import io.limkhashing.omdbmovie.ui.widget.getAverageColor

@Composable
fun MovieItem(
    movie: Movie,
    navigator: Navigator?
) {
    val imageState = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(movie.poster)
            .size(Size.ORIGINAL)
            .build()
    ).state

    val defaultColor = MaterialTheme.colorScheme.secondaryContainer
    var dominantColor by remember { mutableStateOf(defaultColor) }

    Column(
        modifier = Modifier
            .wrapContentHeight()
            .width(200.dp)
            .padding(8.dp)
            .clip(RoundedCornerShape(28.dp))
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.secondaryContainer,
                        dominantColor
                    )
                )
            )
            .clickable {
                navigator?.push(MovieDetailsScreen(imdbID = movie.imdbID))
            }
    ) {
        if (imageState is AsyncImagePainter.State.Error) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(6.dp)
                    .height(250.dp)
                    .clip(RoundedCornerShape(22.dp))
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    modifier = Modifier.size(70.dp),
                    imageVector = Icons.Rounded.ImageNotSupported,
                    contentDescription = movie.title
                )
            }
        }

        if (imageState is AsyncImagePainter.State.Success) {
            dominantColor = getAverageColor(imageBitmap = imageState.result.drawable.toBitmap().asImageBitmap())

            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(6.dp)
                    .height(250.dp)
                    .clip(RoundedCornerShape(22.dp)),
                painter = imageState.painter,
                contentDescription = movie.title,
                contentScale = ContentScale.Crop
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            modifier = Modifier.padding(start = 16.dp, end = 8.dp),
            text = movie.title ?: "",
            color = Color.White,
            fontSize = 15.sp,
            maxLines = 3
        )

        Spacer(modifier = Modifier.height(12.dp))
    }
}