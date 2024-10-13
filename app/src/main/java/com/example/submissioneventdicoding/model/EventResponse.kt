package com.example.submissioneventdicoding.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class EventResponse(
    val error: Boolean,
    val message: String,
    val listEvents: List<Event>
)

@Parcelize
data class Event(
    val imageLogo: String,
    val name: String,
    val ownerName: String,
    val beginTime: String,
    val quota: Int,
    val registrant: Int, // Pastikan ini adalah Int
    val description: String,
    val link: String,
    val otherField: String // Jika ada field lain
) : Parcelable