package com.example.tmdb_compose_v2.ui.tabs

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.tmdb_compose_v2.ui.components.MovieCard
import com.example.tmdb_compose_v2.ui.components.MovieRow
import com.example.tmdb_compose_v2.ui.components.PageableLazyColumn
import com.example.tmdb_compose_v2.ui.components.PageableLazyRow
import com.example.tmdb_compose_v2.ui.navigation.MOVIE_ENTITY
import com.example.tmdb_compose_v2.ui.navigation.navigateWithSerializable
import com.example.tmdb_compose_v2.ui.popups.ErrorPopup
import com.example.tmdb_compose_v2.viewmodels.HomeViewModel

@Composable
fun HomePage(navController: NavController) {
    val viewModel = hiltViewModel<HomeViewModel>()

    val popularMoviesState by viewModel.popularMoviesState.collectAsState()
    val loadingPopular by viewModel.isLoadingPopular.collectAsState()

    val topRatedMoviesState by viewModel.topRatedMoviesState.collectAsState()
    val loadingTopRated by viewModel.isLoadingTopRated.collectAsState()

    LaunchedEffect(Unit) {
        // to prevent from too much data in the app,
        // we reinit this page's calls if we recompose it.
        if (popularMoviesState.movies.size + topRatedMoviesState.movies.size > 300) {
            viewModel.refreshPage()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Column {
                PageableLazyColumn(
                    currentPage = topRatedMoviesState.page, loadPage = { page ->
                        viewModel.getTopRatedMovies(page)
                    }, totalPages = topRatedMoviesState.totalPages, isLoading = loadingTopRated
                ) {
                    item {
                        Column(Modifier.padding(vertical = 10.dp)) {
                            Text(
                                text = "Popular Movies", style = TextStyle(
                                    color = Color.Black,
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Bold
                                ), modifier = Modifier.padding(10.dp)
                            )
                            PageableLazyRow(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(260.dp),
                                currentPage = popularMoviesState.page,
                                loadPage = { page ->
                                    viewModel.getPopularMovies(page)
                                },
                                totalPages = popularMoviesState.totalPages,
                                isLoading = loadingPopular
                            ) {
                                items(popularMoviesState.movies) {
                                    Box(Modifier.widthIn(max = 170.dp)) {
                                        MovieCard(movie = it, onClick = { movie ->
                                            navController.navigateWithSerializable(
                                                MOVIE_ENTITY,
                                                movie
                                            )
                                        })
                                    }
                                }
                            }
                        }
                        Text(
                            text = "Top Rated Movies", style = TextStyle(
                                color = Color.Black, fontSize = 22.sp, fontWeight = FontWeight.Bold
                            ), modifier = Modifier.padding(10.dp)
                        )
                    }
                    items(topRatedMoviesState.movies) { item ->
                        MovieRow(item, onClick = { movie ->
                            navController.navigateWithSerializable(MOVIE_ENTITY, movie)
                        })
                    }
                }
            }
        }
        ErrorPopup(message = viewModel.errorMessage.value) {
            viewModel.clearErrorMessage()
        }
    }
}

@Preview
@Composable
fun HomePagePreview() {
    HomePage(navController = rememberNavController())
}