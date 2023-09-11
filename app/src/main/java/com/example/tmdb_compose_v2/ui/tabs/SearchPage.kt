package com.example.tmdb_compose_v2.ui.tabs

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.tmdb_compose_v2.navigation.MOVIE_ENTITY
import com.example.tmdb_compose_v2.navigation.navigateWithSerializable
import com.example.tmdb_compose_v2.ui.components.MovieCard
import com.example.tmdb_compose_v2.ui.components.PagableGridLayout
import com.example.tmdb_compose_v2.ui.popups.ErrorPopup
import com.example.tmdb_compose_v2.viewmodels.SearchViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchPage(navController: NavController) {
    val viewModel = hiltViewModel<SearchViewModel>()
    val searchText by viewModel.searchField.collectAsState()
    val isSearching by viewModel.isSearching.collectAsState()
    val searchResultsState by viewModel.searchResultsState.collectAsState()
    val focusManager = LocalFocusManager.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 15.dp)
        ) {
            TextField(
                value = searchText,
                onValueChange = viewModel::onSearchFieldChanges,
                singleLine = true,
                trailingIcon = {
                    Icon(
                        Icons.Default.Clear,
                        contentDescription = null,
                        modifier = Modifier.clickable {
                            viewModel.onSearchFieldChanges("")
                            focusManager.clearFocus()
                        }
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .border(1.dp, Color.Black, shape = RoundedCornerShape(20.dp))
                    .clip(RoundedCornerShape(20.dp)), placeholder = {
                    Text("Search For Movies")
                })
            PagableGridLayout(
                modifier = Modifier.fillMaxSize(),
                maxWidth = 150.dp,
                items = searchResultsState.movies,
                itemComposable = {
                    MovieCard(movie = it, onClick = { movie ->
                        navController.navigateWithSerializable(
                            MOVIE_ENTITY,
                            movie
                        )
                    })
                },
                currentPage = searchResultsState.page,
                totalPages = searchResultsState.totalPages,
                isLoading = isSearching,
                loadPage = {
                    viewModel.searchForMovie(searchText, it)
                }
            )
        }
        ErrorPopup(message = viewModel.errorMessage.value) {
            viewModel.clearErrorMessage()
        }
    }
    LaunchedEffect(searchText) {
        viewModel.searchForMovie(searchText)
    }
}


@Preview
@Composable
fun SearchPagePreview() {
    SearchPage(rememberNavController())
}