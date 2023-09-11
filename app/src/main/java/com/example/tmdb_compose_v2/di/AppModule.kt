package com.example.tmdb_compose_v2.di

import android.app.Application
import androidx.room.Room
import com.example.tmdb_compose_v2.storage.FavoriteMovieDao
import com.example.tmdb_compose_v2.storage.FavoriteMovieDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    @Singleton
    fun provideFavoriteMovieDao(appContext: Application): FavoriteMovieDao {
        return Room.databaseBuilder(
            appContext,
            FavoriteMovieDatabase::class.java,
            "favorites.db"
        ).build().dao
    }
}