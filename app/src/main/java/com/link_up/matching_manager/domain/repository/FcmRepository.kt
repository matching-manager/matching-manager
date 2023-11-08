package com.link_up.matching_manager.domain.repository

import com.link_up.matching_manager.ui.fcm.send.Payload
import okhttp3.ResponseBody
import retrofit2.Response

interface FcmRepository {
    suspend fun pushNotification(
        payload : Payload
    ): Response<ResponseBody>
}