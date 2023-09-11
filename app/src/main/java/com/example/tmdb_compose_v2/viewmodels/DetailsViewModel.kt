package com.example.tmdb_compose_v2.viewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tmdb_compose_v2.model.Movie
import com.example.tmdb_compose_v2.storage.FavoriteMovieDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val favoriteMovieDao: FavoriteMovieDao
): ViewModel() {

    private val _isFavorite = mutableStateOf(false)
    val isFavorite: MutableState<Boolean> = _isFavorite

    fun addToFavorites(movie: Movie) {
        viewModelScope.launch {
            favoriteMovieDao.upsertMovie(movie)
            checkIfIsFavorite(movie)
        }
    }

    fun removeFromFavorites(movie: Movie) {
        viewModelScope.launch {
            favoriteMovieDao.deleteMovie(movie)
            checkIfIsFavorite(movie)
        }
    }

    fun checkIfIsFavorite(movie: Movie) {
        viewModelScope.launch {
            _isFavorite.value = favoriteMovieDao.doesMovieExist(movie.id)
        }
    }

}