package com.example.submissioneventdicoding.api

import com.example.submissioneventdicoding.model.EventResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface EventApiService {
    @GET("/events")
    fun getActiveEvents(@Query("active") active: Int): Call<EventResponse>

    @GET("/events")
    fun getCompletedEvents(@Query("active") active: Int): Call<EventResponse>

    @GET("/events")
    fun searchEvents(
        @Query("active") active: Int,
        @Query("q") keyword: String
    ): Call<EventResponse>

}
