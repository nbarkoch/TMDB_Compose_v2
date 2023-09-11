package com.example.tmdb_compose_v2.api

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object MoviesRetrofit {

    private const val BASE_URL = "https://api.themoviedb.org/3/"
    private const val ACCESS_TOKEN =
        "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJiMTI3YjJkM2E0ZDFjYjc4ZGE1YTk0YzFhZjg4NzA5NSIsInN1YiI6IjY0ZjRkNGEyOGMyMmMwMDBhY2ZjZmQ4ZSIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.VnVATjbvXBDOigvxW8WEbOkqDR1FqbGNsmVks5puFNA"
    const val IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w220_and_h330_face/"

    private val okHttpClient: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val requestWithHeaders = chain.request().newBuilder()
                .addHeader("accept", "application/json")
                .addHeader(
                    "Authorization",
                    "Bearer $ACCESS_TOKEN"
                )
                .build()
            chain.proceed(requestWithHeaders)
        }.build()

    val api: IMoviesRetrofit by lazy {
        Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(IMoviesRetrofit::class.java)
    }

}