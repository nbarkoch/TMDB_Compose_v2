package com.example.tmdb_compose_v2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.example.tmdb_compose_v2.model.Movie
import com.example.tmdb_compose_v2.navigation.MOVIE_ENTITY
import com.example.tmdb_compose_v2.navigation.NavigationScreen
import com.example.tmdb_compose_v2.storage.FavoriteMovieDatabase
import com.example.tmdb_compose_v2.ui.screens.DetailsScreen
import com.example.tmdb_compose_v2.ui.screens.MainScreen
import com.example.tmdb_compose_v2.ui.theme.TMDBComposeTheme
import com.example.tmdb_compose_v2.viewmodels.DetailsViewModel
import com.example.tmdb_compose_v2.viewmodels.FavoritesViewModel

class MainActivity : ComponentActivity() {

    private val db by lazy {
        Room.databaseBuilder(
            applicationContext,
            FavoriteMovieDatabase::class.java,
            "favorites.db"
        ).build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lateinit var navController: NavHostController
        val detailsViewModel = DetailsViewModel(db.dao)
        val favoriteViewModel = FavoritesViewModel(db.dao)

        setContent {
            TMDBComposeTheme {
                navController = rememberNavController()
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
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
                                DetailsScreen(movie = movie, viewModel = detailsViewModel)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TMDBComposeTheme {
        Greeting("Android")
    }
}