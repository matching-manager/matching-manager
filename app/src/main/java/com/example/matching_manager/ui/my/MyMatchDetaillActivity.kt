package com.example.matching_manager.ui.my

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.matching_manager.R
import com.example.matching_manager.databinding.MyMatchDetailActivityBinding

class MyMatchDetaillActivity : AppCompatActivity() {
    private lateinit var binding : MyMatchDetailActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MyMatchDetailActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}