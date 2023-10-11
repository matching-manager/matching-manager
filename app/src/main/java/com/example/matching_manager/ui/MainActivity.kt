package com.example.matching_manager.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.matching_manager.databinding.MainActivityBinding


class MainActivity : AppCompatActivity() {

    private val binding by lazy { MainActivityBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}