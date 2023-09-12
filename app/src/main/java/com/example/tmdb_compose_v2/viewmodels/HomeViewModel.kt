package com.example.tmdb_compose_v2.viewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tmdb_compose_v2.model.api.MoviesRetrofit
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.IOException

class HomeViewModel : ViewModel() {

    private val _errorMessage = mutableStateOf<String?>(null)
    val errorMessage: MutableState<String?> = _errorMessage
    fun clearErrorMessage() {
        _errorMessage.value = null
    }

    // Popular Movies:
    private val _popularMoviesState = MutableStateFlow(MoviesState(1, 0, emptyList()))
    val popularMoviesState = _popularMoviesState.asStateFlow()

    private val _isLoadingPopular = MutableStateFlow(false)
    val isLoadingPopular = _isLoadingPopular.asStateFlow()

    // Top Rated Movies:
    private val _topRatedMoviesState = MutableStateFlow(MoviesState(1, 0, emptyList()))
    val topRatedMoviesState = _topRatedMoviesState.asStateFlow()

    private val _isLoadingTopRated = MutableStateFlow(false)
    val isLoadingTopRated = _isLoadingTopRated.asStateFlow()

    init {
        getTopRatedMovies()
        getPopularMovies()
    }

    fun getPopularMovies(page: Int = 1) {
        viewModelScope.launch {
            _isLoadingPopular.value = true

            val response = try {
                MoviesRetrofit.api.getPopular(page = page)
            } catch (e: IOException) {
                _errorMessage.value = "Connection Failure\nCheck if device is connected.."
                e.printStackTrace()
                return@launch
            } catch (e: Exception) {
                _errorMessage.value = "Unexpected Error Occurred\nTry again later.."
                e.printStackTrace()
                return@launch
            } finally {
                _isLoadingPopular.value = false
            }
            if (response.isSuccessful) {
                response.body()?.let { body ->
                    val clonedList = _topRatedMoviesState.value.movies.toMutableList()
                    clonedList.addAll(body.results)
                    _popularMoviesState.value = MoviesState(page, body.totalPages, clonedList)
                }
            } else {
                _errorMessage.value = "Unexpected Error Occurred\nTry again later.."
                _isLoadingPopular.value = false
            }
        }
    }

    fun getTopRatedMovies(page: Int = 1) {
        viewModelScope.launch {
            _isLoadingTopRated.value = true

            val response = try {
                MoviesRetrofit.api.getPopular(page = page)
            } catch (e: IOException) {
                _errorMessage.value = "Connection Failure\nCheck if device is connected.."
                e.printStackTrace()
                return@launch
            } catch (e: Exception) {
                _errorMessage.value = "Unexpected Error Occurred\nTry again later.."
                e.printStackTrace()
                return@launch
            } finally {
                _isLoadingTopRated.value = false
            }
            if (response.isSuccessful) {
                response.body()?.let { body ->
                    if (page > 1) {
                        val clonedList = _topRatedMoviesState.value.movies.toMutableList()
                        clonedList.addAll(body.results)
                        _topRatedMoviesState.value = MoviesState(page, body.totalPages, clonedList)
                    } else {
                        _topRatedMoviesState.value = MoviesState(page, body.totalPages, body.results)
                    }
                }
            } else {
                _errorMessage.value = "Unexpected Error Occurred\nTry again later.."
                _isLoadingTopRated.value = false
            }
        }
    }

    fun refreshPage() {
        _popularMoviesState.value = MoviesState(1, 0, emptyList())
        _topRatedMoviesState.value = MoviesState(1, 0, emptyList())
        getTopRatedMovies()
        getPopularMovies()
    }
}