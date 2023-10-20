package com.example.matching_manager.ui.my

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.matching_manager.databinding.MyMatchDetailActivityBinding

class MyMatchDetailActivity : AppCompatActivity() {
    private lateinit var binding : MyMatchDetailActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MyMatchDetailActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}