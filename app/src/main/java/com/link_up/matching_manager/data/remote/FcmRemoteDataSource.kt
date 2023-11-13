package com.link_up.matching_manager.data.remote

import com.link_up.matching_manager.ui.fcm.send.Payload
import com.link_up.matching_manager.data.retrofit.fcm.FcmRetrofitClient.SENDER_ID
import com.link_up.matching_manager.data.retrofit.fcm.FcmRetrofitClient.SERVER_KEY
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.HeaderMap
import retrofit2.http.Headers
import retrofit2.http.POST

interface FcmRemoteDataSource {
    @POST("fcm/send")
    suspend fun postNotification(
        @HeaderMap headers: Map<String, String> = mapOf( "Authorization" to "key=$SERVER_KEY", "project_id" to SENDER_ID ),
        @Body payload: Payload
    ): retrofit2.Response<ResponseBody>
}
