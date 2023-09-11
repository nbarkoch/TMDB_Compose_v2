package com.example.tmdb_compose_v2.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.tmdb_compose_v2.ui.tabs.TabContent
import com.example.tmdb_compose_v2.ui.tabs.TabHeader
import com.example.tmdb_compose_v2.ui.tabs.TabItem

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainScreen(navController: NavController) {
    val tabs = listOf(TabItem.Home, TabItem.Search, TabItem.Favorite)
    val pagerState = rememberPagerState { tabs.size }
    Column(Modifier.fillMaxSize()) {
        TabHeader(tabs = tabs, pagerState = pagerState)
        TabContent(tabs = tabs, pagerState = pagerState, navController = navController)
    }
}

@Preview
@Composable
fun MainScreenPreview() {
    MainScreen(navController = rememberNavController())
}