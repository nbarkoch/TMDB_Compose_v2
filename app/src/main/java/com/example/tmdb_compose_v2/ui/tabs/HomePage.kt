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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.tmdb_compose_v2.navigation.MOVIE_ENTITY
import com.example.tmdb_compose_v2.navigation.navigateWithSerializable
import com.example.tmdb_compose_v2.ui.components.CollectionPagableRow
import com.example.tmdb_compose_v2.ui.components.LazyColumnPagable
import com.example.tmdb_compose_v2.ui.components.MovieCard
import com.example.tmdb_compose_v2.ui.components.MovieRow
import com.example.tmdb_compose_v2.viewmodels.HomeViewModel

@Composable
fun HomePage(navController: NavController) {
    val viewModel = hiltViewModel<HomeViewModel>()

    val popularMoviesState by viewModel.popularMoviesState.collectAsState()
    val loadingPopular by viewModel.isLoadingPopular.collectAsState()

    val topRatedMoviesState by viewModel.topRatedMoviesState.collectAsState()
    val loadingTopRated by viewModel.isLoadingTopRated.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp)
        ) {
            Column {
                LazyColumnPagable(
                    currentPage = topRatedMoviesState.page, loadPage = { page ->
                        viewModel.getTopRatedMovies(page)
                    }, totalPages = topRatedMoviesState.totalPages, isLoading = loadingTopRated
                ) {
                    item {
                        Column(Modifier.padding(vertical = 10.dp)) {
                            Text(
                                text = "Popular Movies", style = TextStyle(
                                    color = Color.Black,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold
                                ), modifier = Modifier.padding(10.dp)
                            )
                            CollectionPagableRow(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(260.dp),
                                items = popularMoviesState.movies,
                                itemComposable = {
                                    Box(Modifier.widthIn(max = 170.dp)) {
                                        MovieCard(movie = it, onClick = { movie ->
                                            navController.navigateWithSerializable(
                                                MOVIE_ENTITY,
                                                movie
                                            )
                                        })
                                    }
                                },
                                currentPage = popularMoviesState.page,
                                loadPage = { page ->
                                    viewModel.getPopularMovies(page)
                                },
                                totalPages = popularMoviesState.totalPages,
                                isLoading = loadingPopular
                            )
                        }
                        Text(
                            text = "Top Rated Movies", style = TextStyle(
                                color = Color.Black, fontSize = 20.sp, fontWeight = FontWeight.Bold
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
    }
}