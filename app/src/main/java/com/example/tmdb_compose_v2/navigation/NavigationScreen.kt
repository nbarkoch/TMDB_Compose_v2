package com.example.tmdb_compose_v2.navigation

const val MOVIE_ENTITY = "movie"

sealed class NavigationScreen(val route: String) {
    object Main: NavigationScreen("main")
    object Details: NavigationScreen("details")
}