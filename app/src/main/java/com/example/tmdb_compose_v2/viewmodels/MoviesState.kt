package com.example.tmdb_compose_v2.viewmodels

import com.example.tmdb_compose_v2.model.Movie

data class MoviesState(
    val page: Int,
    val totalPages: Int,
    val movies: List<Movie>
)