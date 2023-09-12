# TMDB_Compose_v2

The Project containes 3 tabs and a details screen
each tab is an actual page.

## first tab - Home
including most popular and top-rated movies from the server.
support pagination for both, the horizontal and the vertical.
saving state of scroll on configuration changes.
> clicking on any of the movies will navigate to details screen, with more detail about the selected movie.

## second tab - Search
a textfiled that auto-fetch relevant movies with the corresponding text. display as grid.
has cancel button, support pagination.
saving state of scroll on configuration changes.
> clicking on any of the movies will navigate to details screen, with more detail about the selected movie.

## third tab - Favorites
a vertical list of movies which the user has selected as his favorite.
saving state of scroll on configuration changes.
> clicking on any of the movies will navigate to details screen, with more detail about the selected movie.

Architecture:

I used a Single Activty Application approach, using Jet-Pack Compose only.
MainActivity contains the navigator
the navigator contains the MainScreen and the DetailsScreen
MainScreen contains the 3 pages, (Home, Search, Favorite)
DetailsScreen is relevant only with a movie argument.
each Screen/Page has its own viewModel:
HomePage -> HomeViewModel: contains the popularMoviesState, and topRatedMoviesState, also loading indications.
SearchPage -> SearchViewModel: contains the searchMoviesState, and the searchField state.
FavoritePage -> FavoriteViewModelL contains refernce to DAO, to get all the favorite movies from the user.
DetailsScreen -> DetailsViewModelL contains refernce to DAO, to check if movie is favorite or not, and to set or remove from db.

 >                                HomePage -  viewmodel(API)
 >                              /  
 >                MainScreen --|--SearchPage -  viewmodel(API)
 >              /               \ 
 > MainActivty |                  FavoritePage -  viewmodel(DAO)
 >              \ 
 >                DetailsScreen -  viewmodel(DAO)
         

## Server Requests: 
Retrofit - easier to implement the REST API requests.
data class for Movie, which is Serializable.

## Favorite Movies Storage:
Room Database - most of the fastest ways to get the data.

## Other stuff:
1. Making a Composable that support automatic pagination by scroll.
2. Dependancy Injection - helped with providing the instance of the Dao, and for saving the viewmodel instances wuth the MainActivty lifecycle
3. Implement a function for passing Serializables using the NavController



