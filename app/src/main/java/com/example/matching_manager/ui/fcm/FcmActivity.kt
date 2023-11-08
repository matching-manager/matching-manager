package com.example.matching_manager.ui.fcm

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.example.matching_manager.R
import com.example.matching_manager.databinding.FcmActivityBinding
import com.example.matching_manager.ui.home.alarm.AlarmActivity
import com.example.matching_manager.ui.main.MainActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging

class FcmActivity : AppCompatActivity() {

    private val binding by lazy {
        FcmActivityBinding.inflate(layoutInflater)
    }

    private val viewModel: FcmViewModel by viewModels { FcmViewModelFactory(this) }

    companion object {
        const val TAG = "FcmActivity"

        fun newIntent(context: Context) = Intent(
            context,
            FcmActivity::class.java
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        Log.d(TAG, "this : $this")

//        viewModel.resetData() //시연용 reset 함수
        initFcm()
        initView()
        initViewModel()
    }

    private fun initViewModel() = with(viewModel) {
        list.observe(this@FcmActivity, Observer {
            val intent = AlarmActivity.newIntent(this@FcmActivity)
            startActivity(intent)
            finish()
        })
    }

    private fun initView() = with(binding) {
        //권한 요청
        askNotificationPermission()

        Log.d(TAG, "$TAG 진입")
        Log.d(
            TAG,
            "$TAG fcm Test ${intent.getStringExtra(MyFirebaseMessagingService.RECEIVED_USER_ID)}"
        )

        if (intent.getStringExtra(MyFirebaseMessagingService.RECEIVED_USER_ID) != null) {
            addFcmData()
        } else {
            val intent = Intent(this@FcmActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun addFcmData() {
        val userId = intent.getStringExtra(MyFirebaseMessagingService.RECEIVED_USER_ID)
        val phoneNumber =
            intent.getStringExtra(MyFirebaseMessagingService.RECEIVED_USER_PHONE_NUMBER)
        val body = intent.getStringExtra(MyFirebaseMessagingService.RECEIVED_BODY)
        Log.d(TAG, "$TAG intent value = $userId $phoneNumber $body")
        viewModel.addFcmAlarm(userId, phoneNumber, body)
    }


    // Token 받기
    private fun initFcm() {
        // 알림을 표시할 채널 생성
        val channelId = getString(R.string.default_notification_channel_id)
        val channelName = getString(R.string.default_notification_channel_name)
        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager?.createNotificationChannel(
            NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_LOW,
            ),
        )

//        // 토큰 가져오기
//        Firebase.messaging.token.addOnCompleteListener(
//            OnCompleteListener { task ->
//                if (!task.isSuccessful) {
//                    Log.w(TAG, "FCM 등록 토큰 가져오기 실패", task.exception)
//                    return@OnCompleteListener
//                }
//
//                // 새 FCM 등록 토큰 가져오기
//                val token = task.result
//
//                // 로깅 및 토스트
//                val msg = getString(R.string.msg_token_fmt, token)
//                Log.d(TAG, msg)
//                Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
//                Log.w(TAG, msg, task.exception)
//
//            },
//        )
    }

    // 권한이 있는지 확인하는 함수
    private fun askNotificationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
            PackageManager.PERMISSION_GRANTED
        ) {
            Toast.makeText(this, "알림 권한이 허용되었습니다.", Toast.LENGTH_SHORT)
                .show()
        } else {
            // 권한 직접 요청
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }

    }

    // 권한 요청 결과를 처리하는 런처
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { isGranted: Boolean ->
        if (isGranted) {
            Toast.makeText(this, "알림 권한이 허용되었습니다.", Toast.LENGTH_SHORT)
                .show()
        } else {
            Toast.makeText(
                this,
                "POST_NOTIFICATIONS 권한 없이 FCM 알림을 게시할 수 없습니다",
                Toast.LENGTH_LONG,
            ).show()
        }
    }

}