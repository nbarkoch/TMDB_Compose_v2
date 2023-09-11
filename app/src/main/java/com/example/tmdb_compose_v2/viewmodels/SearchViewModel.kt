package com.example.tmdb_compose_v2.viewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tmdb_compose_v2.api.MoviesRetrofit
import com.example.tmdb_compose_v2.model.MoviesState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.IOException

class SearchViewModel: ViewModel() {

    private val _searchField = MutableStateFlow("")
    val searchField = _searchField.asStateFlow()
    fun onSearchFieldChanges(searchField: String){
        _searchField.value = searchField
    }

    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()

    // Searched Movies:
    private val _searchResultsState = MutableStateFlow(MoviesState(1, 0, emptyList()))
    val searchResultsState = _searchResultsState.asStateFlow()

    private val _errorMessage = mutableStateOf<String?>(null)
    val errorMessage: MutableState<String?> = _errorMessage
    fun clearErrorMessage() {
        _errorMessage.value = null
    }

    fun searchForMovie(query: String, page: Int = 1) {
        viewModelScope.launch {
            if (query.isEmpty()) {
                _isSearching.value = false
                _searchResultsState.value = MoviesState(1, 0, emptyList())
                return@launch
            }

            _isSearching.value = true
            if (page == 1) {
                _searchResultsState.value = MoviesState(1, 0, emptyList())
            }

            val response = try {
                MoviesRetrofit.api.searchForMovie(query = query, page = page)
            } catch (e: IOException) {
                _errorMessage.value = "Connection Failure\nCheck if device is connected.."
                e.printStackTrace()
                return@launch
            } catch (e: Exception) {
                _errorMessage.value = "Unexpected Error Occurred\nTry again later.."
                e.printStackTrace()
                return@launch
            } finally {
                _isSearching.value = false
            }
            if (response.isSuccessful) {
                response.body()?.let { body ->
                    val clonedList = _searchResultsState.value.movies.toMutableList()
                    clonedList.addAll(body.results)
                    _searchResultsState.value= MoviesState(page, body.totalPages, clonedList)
                }
            } else {
                _errorMessage.value = "Unexpected Error Occurred\nTry again later.."
                _isSearching.value = false
            }
        }
    }

}