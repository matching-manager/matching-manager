package com.example.matching_manager.retrofit.fcm

import com.example.matching_manager.data.remote.FcmRemoteDataSource
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object FcmRetrofitClient {
    //    private const val BASE_URL = "https://fcm.googleapis.com/v1/projects/matching-manager/"
    private const val BASE_URL = "https://fcm.googleapis.com"
    const val SERVER_KEY = "AAAAncssPQM:APA91bHiwnBwaHWzLHXB1bxtwqCjgdDojs1MYwmDp9EqudSzYBebkdlOYmQzC5jnXH0HH3m8jTfnRlh0yN0n588_HqRxe8q1KudwCwWM5x26C06dB-Ejt_1Igox0EhhCOk0yhLs0ju3Q"
///        "a0AfB_byBFPnEvMsCmOGC6-XKfOtfZ82uES8bytXTUcJRB-bmVFso2OK2DFq034J1Pt5HggzhZC_XwpGWGHSsTckux9ImoFMWFB_5Fbm5DGEWiwwqlxjBScXw5MjWUsR2fEfNoCR6Hfdj0bl633PizVE0VX6WT0-xFdkbAaCgYKAXkSARMSFQGOcNnC9UgEYRikrZEOk6F9kgjW6g0171"
    const val CONTENT_TYPE = "application/json"
    const val SENDER_ID = "677718539523"

    private val okHttpClient by lazy {
        OkHttpClient.Builder()
            .build()
    }

    private val retrofit by lazy {
        Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: FcmRemoteDataSource by lazy {
        retrofit.create(
            FcmRemoteDataSource::class.java
        )
    }
}