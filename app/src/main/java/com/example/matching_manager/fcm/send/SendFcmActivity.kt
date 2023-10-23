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

//
//class Model(//  "to" changed to token
//    @field:SerializedName("to") var token: String, notification: NotificationModel
//) {
//
//    @SerializedName("notification")
//    private var notification: NotificationModel
//
//    init {
//        this.notification = notification
//    }
//
//    fun getNotification(): NotificationModel {
//        return notification
//    }
//
//    fun setNotification(notification: NotificationModel) {
//        this.notification = notification
//    }
//}
//
//data class NotificationModel(var title: String, var body: String,var user_id:String,var phone_number:String)
//
//interface Api {
//    @Headers(
//        "Authorization: key=" + "a0AfB_byBFPnEvMsCmOGC6-XKfOtfZ82uES8bytXTUcJRB-bmVFso2OK2DFq034J1Pt5HggzhZC_XwpGWGHSsTckux9ImoFMWFB_5Fbm5DGEWiwwqlxjBScXw5MjWUsR2fEfNoCR6Hfdj0bl633PizVE0VX6WT0-xFdkbAaCgYKAXkSARMSFQGOcNnC9UgEYRikrZEOk6F9kgjW6g0171",
//        "Content-Type:application/json"
//    )
//    @POST("fcm/send")
//    fun sendNotification(
//        @Body root: com.example.matching_manager.fcm.send.Model
//    ): Call<ResponseBody?>?
//}
//
//object ApiClient {
//    private const val BASE_URL = "https://fcm.googleapis.com/"
//    private var retrofit: Retrofit? = null
//    val client: Retrofit?
//        get() {
//            if (retrofit == null) {
//                retrofit = Retrofit.Builder()
//                    .baseUrl(BASE_URL)
//                    .addConverterFactory(GsonConverterFactory.create())
//                    .build()
//            }
//            return retrofit
//        }
//}


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

//    private fun initView() {
//        sendNotificationToUser("cTFcTFOo2oZREK1pWPQgpCEztOo2oZREK1pWPQgpCEzt:APA91bECE1onDNtQtkS604DGqGvtb5Xoup0hDJzYU7ITK3u3yA0YZNKs6UUbveJHSB5gw4ZkFHOGVdJ5Ke-6lvclNvVaMLzHm3BIWxc5hD6fUoVLELLi71OYnUcLbaH55clOOWPLpUaU")
//    }
//
//
//    private fun sendNotificationToUser(token: String) {
//        val model = Model(token, NotificationModel("title plz", "body plz","user_id plz", "phone_number plz"))
//        val apiService: Api = ApiClient.client!!.create(Api::class.java)
//        val responseBodyCall: Call<ResponseBody?>? = apiService.sendNotification(model)
//        responseBodyCall?.enqueue(object : Callback<ResponseBody?> {
//            override fun onResponse(call: Call<ResponseBody?>?, response: Response<ResponseBody?>?) {
//                Log.d(TAG, "성공")
//            }
//
//            override fun onFailure(call: Call<ResponseBody?>?, t: Throwable?) {
//                Log.d(TAG, "실패")
//            }
//        })
//    }

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