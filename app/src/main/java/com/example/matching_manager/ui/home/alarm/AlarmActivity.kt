package com.example.matching_manager.ui.home.alarm

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import com.example.matching_manager.databinding.AlarmActivityBinding
import com.example.matching_manager.ui.fcm.send.SendFcmFragment
import com.example.matching_manager.util.Utils

class AlarmActivity : AppCompatActivity() {

    companion object {
        fun newIntent(
            context: Context
        ) = Intent(context, AlarmActivity::class.java)

        const val TAG = "AlarmActivity"
    }

    private val viewModel: AlarmViewModel by viewModels { AlarmViewModelFactory(this) }

    private val listAdapter by lazy {
        AlarmListAdapter(
            onCallClick = { item ->
                item.phoneNumber?.let { Utils.callPhoneNumber(this, it) }
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

    private fun initViewModel() = with(binding) {
        with(viewModel){
            list.observe(this@AlarmActivity, Observer {
                tvAlarm.isVisible = it.isEmpty()
                listAdapter.submitList(it)
            })
        }
    }

    private fun initView() = with(binding) {
        rvAlarm.adapter = listAdapter

        Log.d(TAG, "$TAG 진입 ")
        viewModel.loadAlarm()

        btnBack.setOnClickListener {
            onBackPressed()
        }


    }
}