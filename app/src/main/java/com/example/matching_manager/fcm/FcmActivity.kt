package com.example.matching_manager.fcm

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.example.matching_manager.databinding.FcmActivityBinding
import com.example.matching_manager.ui.home.alarm.AlarmActivity
import com.example.matching_manager.ui.main.MainActivity

class FcmActivity : AppCompatActivity() {

    private val binding by lazy {
        FcmActivityBinding.inflate(layoutInflater)
    }
    companion object{
        const val TAG = "FcmActivity"
    }

    private val viewModel : FcmViewModel by viewModels{ FcmViewModelFactory(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initView()
        initViewModel()
    }

    private fun initViewModel() = with(viewModel){
        list.observe(this@FcmActivity, Observer {
            val intent = AlarmActivity.newIntent(this@FcmActivity)
            startActivity(intent)
        })
    }

    private fun initView() = with(binding) {
        Log.d(TAG,"$TAG 진입")
        Log.d(TAG,"$TAG fcm Test ${intent.extras?.getString("user_id")}")

        if(intent.getStringExtra("user_id") != null){
            addFcmData()
        }else{
            val intent = Intent(this@FcmActivity,MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun addFcmData() {
        val userId = intent.getStringExtra(MyFirebaseMessagingService.RECEIVED_USER_ID)
        val phoneNumber =
            intent.getStringExtra(MyFirebaseMessagingService.RECEIVED_USER_PHONE_NUMBER)
        val body = intent.getStringExtra(MyFirebaseMessagingService.RECEIVED_BODY)
        Log.d(TAG,"$TAG intent value = $userId $phoneNumber $body")
        viewModel.addFcmAlarm(userId,phoneNumber,body)
    }

}