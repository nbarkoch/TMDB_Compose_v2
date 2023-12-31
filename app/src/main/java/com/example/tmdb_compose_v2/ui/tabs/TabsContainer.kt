package com.example.tmdb_compose_v2.ui.tabs

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.Icon
import androidx.compose.material3.LeadingIconTab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TabHeader(tabs: List<TabItem>, pagerState: PagerState) {

    val scope = rememberCoroutineScope()

    TabRow(selectedTabIndex = pagerState.currentPage,
        containerColor = Color.Black,
    ) {
        tabs.forEachIndexed { tabIndex, tabItem ->
            val selectedColor = if (pagerState.currentPage == tabIndex) {
                Color.White
            } else {
                Color.Gray
            }
            LeadingIconTab(
                selected = pagerState.currentPage == tabIndex,
                modifier = Modifier.padding(top = 15.dp),
                onClick = {
                    scope.launch {
                        pagerState.animateScrollToPage(tabIndex)
                    }
                },
                text = { Text(tabItem.title, style = TextStyle(color = selectedColor)) },
                icon = {
                    Icon(
                        imageVector = tabItem.icon,
                        tint = selectedColor,
                        contentDescription = null
                    )
                })
        }
    }
}



@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TabContent(tabs: List<TabItem>, pagerState: PagerState, navController: NavController) {
    HorizontalPager(state = pagerState) { tabIndex ->
        tabs[tabIndex].screen(navController)
    }
}