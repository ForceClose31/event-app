package com.example.submissioneventdicoding.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_events")
data class FavoriteEventEntity(
    @PrimaryKey val id: String,
    val name: String,
    val date: String,
    val description: String,
    val imageLogo: String,
    val quota: Int,
    val registrants: Int,
    val link: String
)
