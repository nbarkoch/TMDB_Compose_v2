package com.example.tmdb_compose_v2.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

@Composable
fun <T> PageableGridLayout(
    modifier: Modifier = Modifier,
    items: List<T>,
    maxWidth: Dp,
    itemComposable: @Composable (T) -> Unit,
    currentPage: Int, loadPage: (page: Int) -> Unit, totalPages: Int, isLoading: Boolean
) {
    val listState = rememberLazyGridState()
    val paginationMutex = remember { Mutex() }
    LazyVerticalGrid(
        columns = GridCells.Adaptive(maxWidth),
        state = listState,
        modifier = modifier) {
        items(items) { item ->
            Box(Modifier.width(maxWidth)) {
                itemComposable(item)
            }
        }
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

    LaunchedEffect(listState.isAtBottom()) {
        if (!isLoading && currentPage <= totalPages) {
            paginationMutex.withLock {
                loadPage(currentPage + 1)
            }
        }
    }
}

@Composable
private fun LazyGridState.isAtBottom(): Boolean {

    return remember(this) {
        derivedStateOf {
            val visibleItemsInfo = layoutInfo.visibleItemsInfo
            if (layoutInfo.totalItemsCount == 0) {
                false
            } else {
                val lastVisibleItem = visibleItemsInfo.last()
                val viewportHeight = layoutInfo.viewportEndOffset + layoutInfo.viewportStartOffset
                (lastVisibleItem.index + 1 == layoutInfo.totalItemsCount &&
                        lastVisibleItem.offset.y + lastVisibleItem.size.height <= viewportHeight)
            }
        }
    }.value
}