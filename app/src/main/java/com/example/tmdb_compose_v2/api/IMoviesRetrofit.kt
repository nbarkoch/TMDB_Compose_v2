package com.example.tmdb_compose_v2.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface IMoviesRetrofit {

    @GET("movie/popular")
    suspend fun getPopular(
        @Query("include_adult") includeAdult: Boolean = false,
        @Query("include_video") includeVideo: Boolean = false,
        @Query("language") language: String = "en-US",
        @Query("page") page: Int = 1,
        @Query("sort_by") sortBy: String = "popularity.desc"
    ): Response<MoviesResponse>

    @GET("movie/top_rated")
    suspend fun getTopRated(
        @Query("language") language: String = "en-US",
        @Query("page") page: Int = 1,
    ): Response<MoviesResponse>

    @GET("search/movie")
    suspend fun searchForMovie(
        @Query("query") query: String,
        @Query("include_adult") includeAdult: Boolean = false,
        @Query("language") language: String = "en-US",
        @Query("page") page: Int = 1
    ): Response<MoviesResponse>

}