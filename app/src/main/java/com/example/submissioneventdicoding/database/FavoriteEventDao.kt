package com.example.submissioneventdicoding.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FavoriteEventDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(favoriteEvent: FavoriteEventEntity)

    @Query("SELECT * FROM favorite_events")
    suspend fun getAllFavorites(): List<FavoriteEventEntity>

    @Query("DELETE FROM favorite_events WHERE id = :id")
    suspend fun deleteById(id: String)
}
