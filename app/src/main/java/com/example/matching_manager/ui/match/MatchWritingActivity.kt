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
import androidx.lifecycle.lifecycleScope
import com.example.matching_manager.R
import com.example.matching_manager.databinding.MatchWritingActivityBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MatchWritingActivity : AppCompatActivity() {

    private lateinit var binding: MatchWritingActivityBinding

    private val viewModel: MatchViewModel by viewModels {
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
            val match = MatchDataModel(matchId = etTeam.text.toString().toInt())

            CoroutineScope(Dispatchers.IO).launch {
                viewModel.addMatch(match)
                viewModel.fetchData()

                // UI 업데이트를 위해 메인 스레드로 스위칭
                withContext(Dispatchers.Main) {
                    viewModel.matchId++
                    finish()
                }
            }
        }


    }
}