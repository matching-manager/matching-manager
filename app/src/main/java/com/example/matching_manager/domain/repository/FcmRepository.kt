package com.example.matching_manager.domain.repository

import com.example.matching_manager.ui.fcm.send.Payload
import okhttp3.ResponseBody
import retrofit2.Response

interface FcmRepository {
    suspend fun pushNotification(
        payload : Payload
    ): Response<ResponseBody>
}