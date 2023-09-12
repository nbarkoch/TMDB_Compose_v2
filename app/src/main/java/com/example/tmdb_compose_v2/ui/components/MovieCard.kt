package com.example.tmdb_compose_v2.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tmdb_compose_v2.model.api.MoviesRetrofit
import com.example.tmdb_compose_v2.model.Movie


@Composable
fun MovieCard(movie: Movie, onClick: (movie: Movie) -> Unit) {
    Column(
        Modifier
            .background(color = Color.White)
            .fillMaxWidth()
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier
                .width(150.dp)
                .padding(8.dp),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 5.dp,
            ),
            shape = RoundedCornerShape(10.dp),
        ) {
            CachedImage(
                url = "${MoviesRetrofit.IMAGE_BASE_URL}${movie.posterPath}",
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .height(200.dp)
                    .width(150.dp)
                    .clip(
                        shape = RoundedCornerShape(topEnd = 10.dp, bottomEnd = 10.dp)
                    )
                    .clickable(onClick = {
                        onClick(movie)
                    })
            )
        }

        Text(
            text = movie.title, fontSize = 18.sp,
            style = TextStyle(
                color = Color.Black,
                fontWeight = FontWeight.Bold,
            ),
            textAlign = TextAlign.Center, maxLines = 2,
            overflow = TextOverflow.Ellipsis,
        )

    }
}

@Composable
@Preview
fun MovieCardPreview() {
    MovieCard(movie = moviePreviewMock, onClick = {})
}