package com.example.matching_manager.ui.my

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import com.example.matching_manager.R
import com.example.matching_manager.databinding.MyMatchEditActivityBinding
import com.example.matching_manager.ui.match.MatchDataModel
import com.example.matching_manager.ui.match.MatchFragment
import com.example.matching_manager.ui.match.MatchViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MyMatchEditActivity : AppCompatActivity() {
    private lateinit var binding: MyMatchEditActivityBinding

    private val viewModel: MyViewModel by viewModels {
        MyMatchViewModelFactory()
    }

    private val data: MyMatchDataModel? by lazy {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(OBJECT_DATA, MyMatchDataModel::class.java)
        }
        else {
            intent.getParcelableExtra<MyMatchDataModel>(OBJECT_DATA)
        }
    }

    companion object {
        const val OBJECT_DATA = "item_object"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MyMatchEditActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()

        viewModel.event.observe(this) {
            when (it) {
                is MatchEvent.Finish -> {
                    finish()
                }
                is MatchEvent.Dismiss -> {
                }
            }
        }

    }

    private fun initView() = with(binding) {

        var selectedGame = ""
        var selectedGender = ""

        // spinner adapter
        //종목 스피너
        val gameAdapter = ArrayAdapter.createFromResource(
            this@MyMatchEditActivity,
            R.array.game_array,
            android.R.layout.simple_spinner_item
        )
        gameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        gameSpinner.adapter = gameAdapter

        //성별 스피너
        val genderAdapter = ArrayAdapter.createFromResource(
            this@MyMatchEditActivity,
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

        val teamName = etTeamName.text.toString()
        val game = selectedGame
        val schedule = etSchedule.text.toString()
        val playerNum = 11
        val matchPlace = etMatchPlace.text.toString()
        val gender = selectedGender
        val entryFee = 10000
        val description = etDiscription.text.toString()

        btnConfirm.setOnClickListener {
            val dummyEditData = MyMatchDataModel(matchId = etTeamName.text.toString().toInt(), schedule = etSchedule.text.toString())
            val editData = MyMatchDataModel(teamName = teamName, game = game, schedule = schedule, matchPlace = matchPlace, playerNum = playerNum, entryFee = entryFee, description = description, gender = gender, viewCount = 0, chatCount = 0)

            val intent = Intent(this@MyMatchEditActivity, MyFragment::class.java)
            setResult(RESULT_OK, intent)
            viewModel.editMatch(data!!, dummyEditData)  //더미데이터




    }
}}