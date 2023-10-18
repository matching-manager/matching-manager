package com.example.matching_manager.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.matching_manager.R
import com.example.matching_manager.databinding.MainActivityBinding
import com.example.matching_manager.fcm.MyFirebaseMessagingService
import com.example.matching_manager.ui.home.alarm.AlarmActivity
import com.google.android.material.tabs.TabLayoutMediator


class MainActivity : AppCompatActivity() {

    private val binding by lazy { MainActivityBinding.inflate(layoutInflater) }
    private val viewPagerAdapter by lazy { MainViewPagerAdapter(this@MainActivity) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initView()
    }

    private fun initView() = with(binding) {
        //view pager adapter
        binding.viewPager.adapter = viewPagerAdapter

        // TabLayout x ViewPager2
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.setText(viewPagerAdapter.getTitle(position))
        }.attach()

        //TabLayout icon 설정
        tabLayout.getTabAt(0)?.setIcon(R.drawable.ic_home_grey)
        tabLayout.getTabAt(1)?.setIcon(R.drawable.ic_match_grey)
        tabLayout.getTabAt(2)?.setIcon(R.drawable.ic_calendar_grey)
        tabLayout.getTabAt(3)?.setIcon(R.drawable.ic_team_grey)
        tabLayout.getTabAt(4)?.setIcon(R.drawable.ic_my_grey)

        // background -> Intent -> Alarm
        intent?.let {
            Log.d("MainActivity","${intent.extras}")
            val userId = intent.extras?.getString("user_id")
            val phoneNumber = intent.extras?.getString("phone_number")
            val body = intent.extras?.getString("body")
            Log.d("MainActivity","value : $userId, $phoneNumber $body")

            val intent = Intent(this@MainActivity,AlarmActivity::class.java)
            intent.apply {
                intent.putExtra(MyFirebaseMessagingService.RECEIVED_USER_ID,userId)
                intent.putExtra(MyFirebaseMessagingService.RECEIVED_USER_PHONE_NUMBER,phoneNumber)
                intent.putExtra(MyFirebaseMessagingService.RECEIVED_BODY,phoneNumber)
            }
            if (userId != null){
                startActivity(intent)
            }
        }
    }

//    override fun onNewIntent(intent: Intent?) {
//        super.onNewIntent(intent)
//        Log.d("MainActivity","intent")
//        intent?.let {
//            Log.d("MainActivity","${intent.extras}")
//
//            val userId = intent.getStringExtra(MyFirebaseMessagingService.RECEIVED_USER_ID)
//            val userPhoneNumber = intent.getStringExtra(MyFirebaseMessagingService.RECEIVED_USER_PHONE_NUMBER)
//            val body = intent.getStringExtra(MyFirebaseMessagingService.RECEIVED_BODY)
//            Log.d("MainActivity","value $userId $userPhoneNumber $body")
//        }
//    }
}