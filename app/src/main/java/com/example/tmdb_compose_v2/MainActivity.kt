package com.example.tmdb_compose_v2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.tmdb_compose_v2.model.Movie
import com.example.tmdb_compose_v2.navigation.MOVIE_ENTITY
import com.example.tmdb_compose_v2.navigation.NavigationScreen
import com.example.tmdb_compose_v2.ui.screens.DetailsScreen
import com.example.tmdb_compose_v2.ui.screens.MainScreen
import com.example.tmdb_compose_v2.ui.theme.TMDBComposeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lateinit var navController: NavHostController

        setContent {
            TMDBComposeTheme {
                navController = rememberNavController()
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.White
                ) {
                    NavHost(
                        navController = navController,
                        startDestination = NavigationScreen.Main.route
                    ) {
                        composable(route = NavigationScreen.Main.route) {
                            MainScreen(navController = navController)
                        }
                        composable(route = NavigationScreen.Details.route) {
                            (it.arguments?.getSerializable(MOVIE_ENTITY) as Movie?)?.let { movie ->
                                DetailsScreen(movie = movie)
                            }
                        }
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun MainActivityPreview() {
    val navController = rememberNavController()
    TMDBComposeTheme {
        NavHost(
            navController = navController,
            startDestination = NavigationScreen.Main.route
        ) {
            composable(route = NavigationScreen.Main.route) {
                MainScreen(navController = navController)
            }
            composable(route = NavigationScreen.Details.route) {
                (it.arguments?.getSerializable(MOVIE_ENTITY) as Movie?)?.let { movie ->
                    DetailsScreen(movie = movie)
                }
            }
        }
    }
}