package com.example.tmdb_compose_v2.viewmodels

import androidx.lifecycle.ViewModel
import com.example.tmdb_compose_v2.storage.FavoriteMovieDao

class FavoritesViewModel(
    private val favoriteMovieDao: FavoriteMovieDao
): ViewModel() {

}