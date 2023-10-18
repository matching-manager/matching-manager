package com.example.matching_manager.ui.home.alarm

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.matching_manager.databinding.AlarmActivityBinding

class AlarmActivity : AppCompatActivity() {
    private val binding by lazy { AlarmActivityBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}