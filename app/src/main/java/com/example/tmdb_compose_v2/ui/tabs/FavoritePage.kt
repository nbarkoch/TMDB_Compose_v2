package com.example.tmdb_compose_v2.ui.tabs

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.tmdb_compose_v2.navigation.MOVIE_ENTITY
import com.example.tmdb_compose_v2.navigation.navigateWithSerializable
import com.example.tmdb_compose_v2.ui.components.MovieRow
import com.example.tmdb_compose_v2.viewmodels.FavoritesViewModel

@Composable
fun FavoritePage(navController: NavController) {
    val viewModel = hiltViewModel<FavoritesViewModel>()
    val favorites = viewModel.favoriteMovies.collectAsState().value
    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn {
            items(favorites) { movie ->
                MovieRow(movie = movie, onClick = {
                    navController.navigateWithSerializable(MOVIE_ENTITY, movie)
                })
            }
        }
    }
}

@Preview
@Composable
fun FavoritePagePreview() {
    FavoritePage(navController = rememberNavController())
}