package com.example.tmdb_compose_v2.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

@Composable
private fun LazyListState.didReachEnd(): Boolean {

    return remember(this) {
        derivedStateOf {
            val visibleItemsInfo = layoutInfo.visibleItemsInfo
            if (layoutInfo.totalItemsCount == 0) {
                false
            } else {
                val lastVisibleItem = visibleItemsInfo.last()
                val viewportHeight = layoutInfo.viewportEndOffset + layoutInfo.viewportStartOffset
                (lastVisibleItem.index + 1 == layoutInfo.totalItemsCount &&
                        lastVisibleItem.offset + lastVisibleItem.size <= viewportHeight)
            }
        }
    }.value
}

@Composable
fun PageableLazyRow(
    modifier: Modifier = Modifier,
    currentPage: Int, loadPage: (page: Int) -> Unit, totalPages: Int, isLoading: Boolean,
    listLazyScope: LazyListScope.() -> Unit
) {
    val listState = rememberLazyListState()
    val paginationMutex = remember { Mutex() }
    LazyRow(
        state = listState,
        modifier = modifier
    ) {
        listLazyScope()
        if (isLoading) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }

    LaunchedEffect(listState.didReachEnd()) {
        if (!isLoading && currentPage <= totalPages) {
            // make sure that we aren't already loading new page,
            // by using mutex, and check state of isLoading,
            // and that there are more pages left to fetch
            paginationMutex.withLock {
                loadPage(currentPage + 1)
            }
        }
    }
}


@Composable
fun PageableLazyColumn(
    modifier: Modifier = Modifier,
    currentPage: Int, loadPage: (page: Int) -> Unit, totalPages: Int, isLoading: Boolean,
    listLazyScope: LazyListScope.() -> Unit
) {
    val listState = rememberLazyListState()
    val paginationMutex = remember { Mutex() }
    LazyColumn(
        state = listState,
        modifier = modifier
    ) {
        listLazyScope()
        if (isLoading) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }

    LaunchedEffect(listState.didReachEnd()) {
        if (!isLoading && currentPage <= totalPages) {
            // make sure that we aren't already loading new page,
            // by using mutex, and check state of isLoading,
            // and that there are more pages left to fetch
            paginationMutex.withLock {
                loadPage(currentPage + 1)
            }
        }
    }
}