package com.example.tmdb_compose_v2.navigation

import android.os.Bundle
import androidx.core.net.toUri
import androidx.navigation.NavController
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.NavDestination
import java.io.Serializable

fun navigateWithSerializable(
    navController: NavController,
    key: String,
    serializable: Serializable
) {
    val routeLink = NavDeepLinkRequest
        .Builder
        .fromUri(NavDestination.createRoute(NavigationScreen.Details.route).toUri())
        .build()
    navController.graph.matchDeepLink(routeLink)?.run {
        val bundle = Bundle()
        bundle.putSerializable(key, serializable)
        navController.navigate(destination.id, bundle)
    }
}