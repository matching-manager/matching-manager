package com.example.matching_manager.ui.home.arena

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.example.matching_manager.databinding.ArenaActivityBinding

class ArenaActivity : AppCompatActivity() {

    private val binding by lazy { ArenaActivityBinding.inflate(layoutInflater) }

    private val viewModel : ArenaViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initView()
        initModel()
    }

    private fun initView() = with(binding){
    }

    private fun initModel() = with(viewModel){


    }
}