package com.example.tmdb_compose_v2.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tmdb_compose_v2.model.Movie
import com.example.tmdb_compose_v2.storage.FavoriteMovieDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel  @Inject constructor(
    private val favoriteMovieDao: FavoriteMovieDao
): ViewModel() {

    private val _favoriteMovies = MutableStateFlow(emptyList<Movie>())
    val favoriteMovies = _favoriteMovies.asStateFlow()

    init {
        viewModelScope.launch {
            favoriteMovieDao.getAllMovies()
                .collect { movies ->
                    _favoriteMovies.value = movies
                }
        }
    }
}