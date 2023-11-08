package com.link_up.matching_manager.retrofit.fcm

import com.link_up.matching_manager.data.remote.FcmRemoteDataSource
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object FcmRetrofitClient {
    //    private const val BASE_URL = "https://fcm.googleapis.com/v1/projects/matching-manager/"
    private const val BASE_URL = "https://fcm.googleapis.com"
    const val SERVER_KEY = "AAAAncssPQM:APA91bHiwnBwaHWzLHXB1bxtwqCjgdDojs1MYwmDp9EqudSzYBebkdlOYmQzC5jnXH0HH3m8jTfnRlh0yN0n588_HqRxe8q1KudwCwWM5x26C06dB-Ejt_1Igox0EhhCOk0yhLs0ju3Q"
//    const val SENDER_ID = "fIskCPLNRp-71t5ZiiRePL:APA91bESLiMhccFyoDeu2jFgtPRNbQdBKDgoAAOwe22KEbZ6OBmNes11eKz_hgPWctEam87vRTABnj14Fa2-XtztmpxHPDNGxbElEBMjjVgU7usW2VqFh3leqHrY8QSTLlATgMb0_KsC"
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

    val api: com.link_up.matching_manager.data.remote.FcmRemoteDataSource by lazy {
        retrofit.create(
            com.link_up.matching_manager.data.remote.FcmRemoteDataSource::class.java
        )
    }
}