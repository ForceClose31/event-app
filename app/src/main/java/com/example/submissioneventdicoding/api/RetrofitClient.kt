package com.example.submissioneventdicoding.api

import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "https://event-api.dicoding.dev/"

    private val loggingInterceptor = HttpLoggingInterceptor { message ->
        Log.d("API Request", message)
    }.apply {
        level = HttpLoggingInterceptor.Level.BODY // Menampilkan body request/response
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor) // Tambahkan interceptor logging
        .build()

    val instance: EventApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient) // Menggunakan OkHttpClient yang sudah ditambahkan interceptor
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(EventApiService::class.java)
    }
}
