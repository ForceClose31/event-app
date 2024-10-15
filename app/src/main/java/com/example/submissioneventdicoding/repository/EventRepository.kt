package com.example.submissioneventdicoding.repository

import com.example.submissioneventdicoding.database.AppDatabase
import com.example.submissioneventdicoding.database.FavoriteEventEntity

class EventRepository(private val database: AppDatabase) {

    suspend fun addFavorite(event: FavoriteEventEntity) {
        database.favoriteEventDao().insert(event)
    }

    suspend fun getAllFavorites(): List<FavoriteEventEntity> {
        return database.favoriteEventDao().getAllFavorites()
    }

    suspend fun removeFavorite(id: String) {
        database.favoriteEventDao().deleteById(id)
    }
}
