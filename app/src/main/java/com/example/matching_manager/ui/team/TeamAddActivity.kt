package com.example.matching_manager.ui.team

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import com.example.matching_manager.R
import com.example.matching_manager.databinding.TeamAddActivityBinding
import com.example.matching_manager.ui.team.bottomsheet.TeamAge
import com.example.matching_manager.ui.team.bottomsheet.TeamFilterCategory
import com.example.matching_manager.ui.team.bottomsheet.TeamNumber

class TeamAddActivity : AppCompatActivity() {
    private lateinit var binding: TeamAddActivityBinding
    private var selectedAge: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = TeamAddActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSpinner()
        initView()
    }

    private fun setSpinner()= with(binding) {
        // spinner adapter
        //종목 스피너
        val gameAdapter = ArrayAdapter.createFromResource(
            this@TeamAddActivity,
            R.array.game_array,
            android.R.layout.simple_spinner_item
        )
        gameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        gameSpinner.adapter = gameAdapter

        //위치 스피너
        val areaAdapter = ArrayAdapter.createFromResource(
            this@TeamAddActivity,
            R.array.area_array,
            android.R.layout.simple_spinner_item
        )
        areaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        areaSpinner.adapter = areaAdapter

        //성별 스피너
        val genderAdapter = ArrayAdapter.createFromResource(
            this@TeamAddActivity,
            R.array.gender_array,
            android.R.layout.simple_spinner_item
        )
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        genderSpinner.adapter = genderAdapter

        //실력 스피너
        val levelAdapter = ArrayAdapter.createFromResource(
            this@TeamAddActivity,
            R.array.level_array,
            android.R.layout.simple_spinner_item
        )
        levelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        levelSpinner.adapter = levelAdapter

        //실력 스피너
        val timeAdapter = ArrayAdapter.createFromResource(
            this@TeamAddActivity,
            R.array.time_array,
            android.R.layout.simple_spinner_item
        )
        timeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        timeSpinner.adapter = timeAdapter

        //number
        teamNumber.setOnClickListener {
            showNumberPicker()
        }
        //age
        ageNumber.setOnClickListener {
            showAgePicker()
        }

    }

    private fun initView() = with(binding) {

        //back button
        btnCancel.setOnClickListener {
            finish() // 현재 Activity 종료
        }

    }

    private fun showNumberPicker() {
        val bottomSheet = TeamNumber()
        bottomSheet.show(supportFragmentManager, "TeamNumberBottomSheet")
    }

    private fun showAgePicker() {
        val bottomSheet = TeamAge()
        bottomSheet.show(supportFragmentManager, "TeamAgeBottomSheet")
    }
}