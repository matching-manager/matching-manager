package com.example.matching_manager.fcm

import android.os.Bundle
import android.util.Log
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

    private val msg = "testMessage!!!!"

//    private val FCM_API = "https://fcm.googleapis.com/v1/projects/myproject-b5ae1/messages:send"
//    private val serverKey =
//        "Bearer" + "BD2jCgWMxrkfOv0O5zVsIO8uS43Wc4f2T8UklZ5DVBajHuAmSVvkrWQ2XU9E2dAZzcyxz1s-7GmYxsIEUM0tEis"
//    private val contentType = "application/json"
//    private val requestQueue: RequestQueue by lazy {
//        Volley.newRequestQueue(this.applicationContext)
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initView()
    }

    private fun initView() = with(binding) {

        runCatching {
                val fcmData = Payload(
                    PayloadMessage(
                        token = "cTFcTFOo2oZREK1pWPQgpCEztOo2oZREK1pWPQgpCEzt:APA91bECE1onDNtQtkS604DGqGvtb5Xoup0hDJzYU7ITK3u3yA0YZNKs6UUbveJHSB5gw4ZkFHOGVdJ5Ke-6lvclNvVaMLzHm3BIWxc5hD6fUoVLELLi71OYnUcLbaH55clOOWPLpUaU",
                        PayloadNotification(
                            body = msg,
                            title = "팀 매칭 알림입니다.",
                            userId = "SoftyChoo",
                            phoneNumber = "01028179282"
                        )
                    )
                )
                pushNotification(Gson().toJson(fcmData))
            }.onFailure { e ->
                Log.e(TAG, "Error: ${e.message}")
            }
    }

    private fun pushNotification(payload: String) = CoroutineScope(Dispatchers.IO).launch {
        FcmRetrofitClient.api.postNotification(payload)
    }


//    private fun sendMessage() {
//        Log.d(TAG, "SendMessage() 진입")
//        if (!TextUtils.isEmpty(msg)) {
//            val notification = JSONObject()
//            val message = JSONObject()
//
//            runCatching {
//                val fcmData = Payload(
//                    PayloadMessage(
//                        token = "cTFcTFOo2oZREK1pWPQgpCEztOo2oZREK1pWPQgpCEzt:APA91bECE1onDNtQtkS604DGqGvtb5Xoup0hDJzYU7ITK3u3yA0YZNKs6UUbveJHSB5gw4ZkFHOGVdJ5Ke-6lvclNvVaMLzHm3BIWxc5hD6fUoVLELLi71OYnUcLbaH55clOOWPLpUaU",
//                        PayloadNotification(
//                            body = msg,
//                            title = "팀 매칭 알림입니다.",
//                            userId = "SoftyChoo",
//                            phoneNumber = "01028179282"
//                        )
//                    )
//                )
//                sendNotification(Gson().toJson(fcmData))
//            }.onFailure { e ->
//                Log.e(TAG, "Error: ${e.message}")
//            }
//        }
//    }
//    private fun sendNotification(payload: String) {
//        Log.d(TAG, "sendNotification value $payload")
//
//
//    }

}