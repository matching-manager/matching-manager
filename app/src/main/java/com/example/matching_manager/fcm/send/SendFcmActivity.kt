package com.example.matching_manager.fcm.send

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.matching_manager.databinding.SendFcmActivityBinding
import com.example.matching_manager.retrofit.fcm.FcmRetrofitClient
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SendFcmActivity : AppCompatActivity() {

    private val binding by lazy { SendFcmActivityBinding.inflate(layoutInflater) }

    companion object {
        const val TAG = "SendFcmActivity"
    }

    private val viewModel: SendFcmViewModel by viewModels { SendFcmViewModelFactory() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initView()
    }

    private fun initView() = with(binding) {

        runCatching {
            val fcmData = Payload(
                "cTFcTFOo2oZREK1pWPQgpCEztOo2oZREK1pWPQgpCEzt:APA91bECE1onDNtQtkS604DGqGvtb5Xoup0hDJzYU7ITK3u3yA0YZNKs6UUbveJHSB5gw4ZkFHOGVdJ5Ke-6lvclNvVaMLzHm3BIWxc5hD6fUoVLELLi71OYnUcLbaH55clOOWPLpUaU"
            )
            pushNotification(fcmData)
        }.onFailure { e ->
            Log.e(TAG, "Error: ${e.message}")
        }
    }

    private fun pushNotification(payload: Payload) = CoroutineScope(Dispatchers.IO).launch {
        FcmRetrofitClient.api.postNotification(
            payload = payload
        )
        viewModel.sendData(payload)

    }
}