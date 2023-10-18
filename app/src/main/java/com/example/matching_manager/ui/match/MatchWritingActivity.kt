package com.example.matching_manager.ui.match

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import com.example.matching_manager.R
import com.example.matching_manager.databinding.MatchWritingActivityBinding

class MatchWritingActivity : AppCompatActivity() {

    private lateinit var binding : MatchWritingActivityBinding

    private val viewModel : MatchViewModel by viewModels {
        MatchViewModelFactory()
    }
    companion object {
        const val ID_DATA = "item_userId"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MatchWritingActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()

    }

    private fun initView() = with(binding) {

        val userId = intent.getStringExtra(ID_DATA)
        Log.d("123", "${userId}")

        btnConfirm.setOnClickListener {
            val match = MatchDataModel(matchId = viewModel.matchId)
            viewModel.addMatch(match)
            viewModel.fetchData()
            viewModel.matchId++
            finish()
        }


    }
}