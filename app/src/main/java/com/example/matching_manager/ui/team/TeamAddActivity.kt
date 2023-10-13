package com.example.matching_manager.ui.team

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.matching_manager.R
import com.example.matching_manager.databinding.TeamAddActivityBinding
import com.example.matching_manager.databinding.TeamDetailActivityBinding
import com.example.matching_manager.ui.team.mdoel.TeamModel

class TeamAddActivity : AppCompatActivity() {
    private lateinit var binding: TeamAddActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = TeamAddActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
    }

    private fun initView() = with(binding) {

        //back button
        btnCancel.setOnClickListener {
            finish() // 현재 Activity 종료
        }
    }
}