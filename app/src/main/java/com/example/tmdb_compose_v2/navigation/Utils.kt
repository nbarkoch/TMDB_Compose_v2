package com.example.tmdb_compose_v2.navigation

import android.os.Bundle
import androidx.core.net.toUri
import androidx.navigation.NavController
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.NavDestination
import java.io.Serializable

/**
 * an extension method for navController that gets
 * @param key of the Serializable parameter,
 * @param serializable the actual value
 * Since Jet-Pack Compose doesn't have a straight way to pass Serializable arguments
 * through the NavController, I found the use of this implementation to pass a Serializable
 * successfully
 * */
fun NavController.navigateWithSerializable(
    key: String,
    serializable: Serializable
) {
    val routeLink = NavDeepLinkRequest
        .Builder
        .fromUri(NavDestination.createRoute(NavigationScreen.Details.route).toUri())
        .build()
    this.graph.matchDeepLink(routeLink)?.run {
        val bundle = Bundle()
        bundle.putSerializable(key, serializable)
        navigate(destination.id, bundle)
    }
}