package com.example.tmdb_compose_v2.ui.tabs

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController

sealed class TabItem(val title: String, val icon: ImageVector, val screen: @Composable (navController: NavController) -> Unit) {
    object Home: TabItem(title = "Home",icon = Icons.Default.Home, screen = { HomePage(it) })
    object Search: TabItem(title = "Search",icon = Icons.Default.Search, screen = { SearchPage(it) })
    object Favorite: TabItem(title = "Favorite",icon = Icons.Default.Favorite, screen = { FavoritePage(it) })
}