package com.example.matching_manager.data.remote

import com.example.matching_manager.retrofit.fcm.FcmRetrofitClient.CONTENT_TYPE
import com.example.matching_manager.retrofit.fcm.FcmRetrofitClient.SERVER_KEY
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface FcmRemoteDataSource {
    @Headers("Authorization: Bearer $SERVER_KEY", "Content-Type:$CONTENT_TYPE")
    @POST("fcm/send")
    suspend fun postNotification(
        @Body payload: String
    ): retrofit2.Response<ResponseBody>
}