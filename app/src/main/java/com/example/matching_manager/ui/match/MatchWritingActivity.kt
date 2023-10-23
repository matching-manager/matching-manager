package com.example.matching_manager.ui.match

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import com.example.matching_manager.R
import com.example.matching_manager.databinding.MatchWritingActivityBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID

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

        viewModel.event.observe(this) {
            when (it) {
                is MatchEvent.Finish -> {
                    finish()
                }
            }
        }
    }

    @SuppressLint("SuspiciousIndentation")
    private fun initView() = with(binding) {

        val userId = intent.getStringExtra(ID_DATA)
        Log.d("MatchWritingActivity", "userId = $userId")

        var selectedGame = ""
        var selectedGender = ""

        // spinner adapter
        //종목 스피너
        val gameAdapter = ArrayAdapter.createFromResource(
            this@MatchWritingActivity,
            R.array.game_array,
            android.R.layout.simple_spinner_item
        )
        gameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        gameSpinner.adapter = gameAdapter

        //성별 스피너
        val genderAdapter = ArrayAdapter.createFromResource(
            this@MatchWritingActivity,
            R.array.gender_array,
            android.R.layout.simple_spinner_item
        )
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        genderSpinner.adapter = genderAdapter

        gameSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectedGame = gameAdapter.getItem(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        genderSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectedGender = gameAdapter.getItem(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        val matchId = UUID.randomUUID().toString()
        val teamName = etTeamName.text.toString()
        val game = selectedGame
        val schedule = etSchedule.text.toString()
        val playerNum = 11
        val matchPlace = etMatchPlace.text.toString()
        val gender = selectedGender
        val entryFee = 10000
        val description = etDiscription.text.toString()
        val uploadTime = getCurrentTime()



        btnConfirm.setOnClickListener {
            val dummyMatch = MatchDataModel(matchId = matchId, schedule = etSchedule.text.toString(), uploadTime = uploadTime)
            val match = MatchDataModel(matchId = matchId,teamName = teamName, game = game, schedule = schedule, matchPlace = matchPlace, playerNum = playerNum, entryFee = entryFee, description = description, gender = gender, viewCount = 0, chatCount = 0, uploadTime = uploadTime)

            val intent = Intent(this@MatchWritingActivity, MatchFragment::class.java)
            setResult(RESULT_OK, intent)

            viewModel.addMatch(dummyMatch)  //더미데이터
//            viewModel.addMatch(match)     //실제 데이터
        }
    }

    private fun getCurrentTime() : String {
        val currentTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

        return currentTime.format(formatter)
    }
}