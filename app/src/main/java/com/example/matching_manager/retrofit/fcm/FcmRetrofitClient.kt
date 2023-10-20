package com.example.matching_manager.retrofit.fcm

import com.example.matching_manager.data.remote.FcmRemoteDataSource
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object FcmRetrofitClient {
    private const val BASE_URL = "https://fcm.googleapis.com/v1/projects/matching-manager/messages:send/"
    const val SERVER_KEY = "Bearer" + "BD2jCgWMxrkfOv0O5zVsIO8uS43Wc4f2T8UklZ5DVBajHuAmSVvkrWQ2XU9E2dAZzcyxz1s-7GmYxsIEUM0tEis"
    const val CONTENT_TYPE = "application/json"

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api : FcmRemoteDataSource by lazy {
        retrofit.create(
           FcmRemoteDataSource::class.java
        )
    }
}