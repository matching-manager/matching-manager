package com.example.matching_manager.ui.home.alarm

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.example.matching_manager.databinding.AlarmActivityBinding
import com.example.matching_manager.fcm.MyFirebaseMessagingService

class AlarmActivity : AppCompatActivity() {

    companion object {
//        fun newIntent(
//            context: Context,
//            phoneNumber:String,
//            userId:String
//        ) = Intent(context,AlarmActivity::class.java).apply {
//            putExtra(MyFirebaseMessagingService.RECEIVED_USER_PHONE_NUMBER, phoneNumber)
//            putExtra(MyFirebaseMessagingService.RECEIVED_USER_ID, userId)
//        }

    }

    private val viewModel: AlarmViewModel by viewModels()

    private val listAdapter by lazy {
        AlarmListAdapter(
            onClick = { item ->

            }
        )
    }

    private val binding by lazy { AlarmActivityBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initView()
        initViewModel()
    }

    private fun initViewModel() = with(viewModel) {
        list.observe(this@AlarmActivity, Observer {
            listAdapter.submitList(it)
        })
    }

    private fun initView() = with(binding) {
        rvAlarm.adapter = listAdapter

        val userId = intent.getStringExtra(MyFirebaseMessagingService.RECEIVED_USER_ID)
        val userPhoneNumber = intent.getStringExtra(MyFirebaseMessagingService.RECEIVED_USER_PHONE_NUMBER)
        val body = intent.getStringExtra(MyFirebaseMessagingService.RECEIVED_BODY)
        Log.d("AlarmActivity","value $userId $userPhoneNumber $body")
        if(userId !=null){
            viewModel.addAlarm(userId, body, userPhoneNumber)
        }
    }

//    override fun onNewIntent(intent: Intent?) {
//        super.onNewIntent(intent)
//        Log.d("AlarmActivity","intent")
//        intent?.let {
//            val userId = intent.getStringExtra(MyFirebaseMessagingService.RECEIVED_USER_ID)
//            val userPhoneNumber = intent.getStringExtra(MyFirebaseMessagingService.RECEIVED_USER_PHONE_NUMBER)
//            val body = intent.getStringExtra(MyFirebaseMessagingService.RECEIVED_BODY)
//            Log.d("AlarmActivity","value $userId $userPhoneNumber $body")
//            viewModel.addAlarm(userId, body, userPhoneNumber)
//        }
//    }
}