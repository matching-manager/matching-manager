package com.link_up.matching_manager.ui.fcm.send

import android.util.Log
import androidx.lifecycle.ViewModel
import com.link_up.matching_manager.domain.repository.FcmRepository

class SendFcmViewModel(
    private val repository: FcmRepository
) : ViewModel() {

    suspend fun sendData(payload: Payload) {
        Log.d("SendFcmViewModel", "SendFcmViewModel-sendData()진입")
        Log.d("SendFcmViewModel", "payLoad : $payload")
        Log.d("SendFcmViewModel", "${repository.pushNotification(payload = payload)}")

        repository.pushNotification(payload = payload)
    }
}