package com.example.tmdb_compose_v2.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest

@Composable
fun CachedImage(url: String, contentDescription: String?) {
    val painter = // Set cacheKey to ensure proper caching based on the URL.
        rememberAsyncImagePainter(ImageRequest.Builder(LocalContext.current).data(data = url).apply<ImageRequest.Builder>(block = fun ImageRequest.Builder.() {
            }).build()
        )

    Image(
        painter = painter,
        contentDescription = contentDescription,
        modifier = Modifier.fillMaxSize()
    )
}