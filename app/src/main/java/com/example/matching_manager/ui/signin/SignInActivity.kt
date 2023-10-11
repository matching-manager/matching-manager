package com.example.matching_manager.ui.signin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.matching_manager.R
import com.example.matching_manager.databinding.SignInActivityBinding

class SignInActivity : AppCompatActivity() {

    private val binding by lazy { SignInActivityBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}