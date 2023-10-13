package com.example.matching_manager.ui.team

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.matching_manager.databinding.MainActivityBinding

class TeamDetailActivity : AppCompatActivity() {
    private val binding by lazy { MainActivityBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initView()
    }

    private fun initView() = with(binding) {


    }
}