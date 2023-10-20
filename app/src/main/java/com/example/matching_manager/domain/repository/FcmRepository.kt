package com.example.matching_manager.domain.repository

import com.example.matching_manager.fcm.Payload
import com.example.matching_manager.fcm.PayloadNotification
import okhttp3.ResponseBody
import retrofit2.Response

interface FcmRepository {
    suspend fun pushNotification(
        payload : String
    ): Response<ResponseBody>
}