package com.example.tmdb_compose_v2.model

data class MoviesState(
    val page: Int,
    val totalPages: Int,
    val movies: List<Movie>
)