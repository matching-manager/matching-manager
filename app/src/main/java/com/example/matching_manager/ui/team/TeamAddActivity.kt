package com.example.matching_manager.ui.team

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.example.matching_manager.R
import com.example.matching_manager.databinding.TeamAddActivityBinding
import com.example.matching_manager.ui.team.bottomsheet.TeamAge
import com.example.matching_manager.ui.team.bottomsheet.TeamNumber

class TeamAddActivity : AppCompatActivity() {
    private lateinit var binding: TeamAddActivityBinding
    private var selectedGame: String? = null
    private var selectedArea: String? = null
    private var selectedGender: String? = null
    private var selectedLevel: String? = null
    private var selectedTime: String? = null
    private var selectedAge: Int? = null
    private var selectedNumber: Int? = null

    //진입타입 설정을 위함
    companion object {

        const val EXTRA_TEAM_ENTRY_TYPE = "extra_team_entry_type"
        const val EXTRA_TEAM_POSITION = "extra_team_position"
        const val EXTRA_TEAM_MODEL = "extra_team_model"

        //용병모집
        fun newIntentForAddRecruit(
            context: Context,
        ) = Intent(context, TeamAddActivity::class.java).apply {
            putExtra(EXTRA_TEAM_ENTRY_TYPE, TeamAddType.RECRUIT.name)
//            putExtra(EXTRA_TEAM_POSITION, position)
//            putExtra(EXTRA_TEAM_MODEL, teamItem)
        }

        //용병신청
        fun newIntentForAddApplication(
            context: Context,
        ) = Intent(context, TeamAddActivity::class.java).apply {
            putExtra(EXTRA_TEAM_ENTRY_TYPE, TeamAddType.APPLICATION.name)
//            putExtra(EXTRA_TEAM_POSITION, position)
//            putExtra(EXTRA_TEAM_MODEL, teamItem)
        }
    }

    private val entryType by lazy {
        TeamAddType.from(intent.getStringExtra(EXTRA_TEAM_ENTRY_TYPE))
    }

    private val teamItem by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent?.getParcelableExtra(
                EXTRA_TEAM_MODEL,
                TeamItem::class.java
            )
        } else {
            intent?.getParcelableExtra<TeamItem>(
                EXTRA_TEAM_MODEL
            )
        }
    }

    private val position by lazy {
        intent.getIntExtra(EXTRA_TEAM_POSITION, -1)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = TeamAddActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d("TeamAddActivity", "in")
        setSpinner()
        initView()
    }

    private fun setSpinner() = with(binding) {
        // spinner adapter
        //종목 스피너
        val gameAdapter = ArrayAdapter.createFromResource(
            this@TeamAddActivity,
            R.array.game_array,
            android.R.layout.simple_spinner_item
        )
        gameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        gameSpinner.adapter = gameAdapter
        gameSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long,
            ) {
                selectedGame = parent?.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
        }

        //위치 스피너
        val areaAdapter = ArrayAdapter.createFromResource(
            this@TeamAddActivity,
            R.array.area_array,
            android.R.layout.simple_spinner_item
        )
        areaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        areaSpinner.adapter = areaAdapter
        areaSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long,
            ) {
                selectedArea = parent?.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
        }


        //성별 스피너
        val genderAdapter = ArrayAdapter.createFromResource(
            this@TeamAddActivity,
            R.array.gender_array,
            android.R.layout.simple_spinner_item
        )
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        genderSpinner.adapter = genderAdapter
        genderSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long,
            ) {
                selectedGender = parent?.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
        }


        //실력 스피너
        val levelAdapter = ArrayAdapter.createFromResource(
            this@TeamAddActivity,
            R.array.level_array,
            android.R.layout.simple_spinner_item
        )
        levelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        levelSpinner.adapter = levelAdapter
        levelSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long,
            ) {
                selectedLevel = parent?.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
        }

        //일정 스피너
        val timeAdapter = ArrayAdapter.createFromResource(
            this@TeamAddActivity,
            R.array.time_array,
            android.R.layout.simple_spinner_item
        )
        timeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        timeSpinner.adapter = timeAdapter
        timeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long,
            ) {
                selectedTime = parent?.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
        }

        //number
        teamNumber.setOnClickListener {
            showNumberPicker()
        }
        //age
        ageNumber.setOnClickListener {
            showAgePicker()
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


    private fun initView() = with(binding) {
        //back button
        btnCancel.setOnClickListener {
            finish() // 현재 Activity 종료
        }

        btnSubmit.setOnClickListener {
            val selectedGame = gameSpinner.selectedItem.toString()
            val selectedArea = areaSpinner.selectedItem.toString()
            val selectedGender = genderSpinner.selectedItem.toString()
            val selectedLevel = levelSpinner.selectedItem.toString()
            val selectedTime = timeSpinner.selectedItem.toString()

            val intent = Intent().apply {
                putExtra(EXTRA_TEAM_ENTRY_TYPE, entryType?.name)
                putExtra(EXTRA_TEAM_POSITION, position)
                putExtra(EXTRA_TEAM_MODEL, teamItem)
            }
            setResult(Activity.RESULT_OK, intent)
            finish()


            val teamItem = when (entryType) {
                TeamAddType.RECRUIT -> {
                    TeamItem.RecruitmentItem(
                        type = TeamAddType.RECRUIT.name, // 임의의 값으로 설정 (용병모집)
                        detail = "",
                        game = selectedGame,
                        area = selectedArea,
                        schedule = selectedTime,
                        teamProfile = 0,
                        playerNum = selectedNumber.toString(),
                        pay = "",
                        teamName = "",
                        gender = selectedGender,
                        viewCount = 0,
                        chatCount = 0,
                        place = "",
                        nicname = "",
                        content = etContent.text.toString(),
                        creationTime = "",
                        level = selectedLevel
                    )
                }

                TeamAddType.APPLICATION -> {
                    TeamItem.ApplicationItem(
                        type = TeamAddType.APPLICATION.name, // 임의의 값으로 설정 (용병신청)
                        game = selectedGame,
                        area = selectedArea,
                        schedule = selectedTime,
                        teamProfile = 0,
                        playerNum = selectedNumber.toString(),
                        age = selectedAge.toString(),
                        gender = selectedGender,
                        viewCount = 0,
                        chatCount = 0,
                        nicname = "",
                        content = etContent.text.toString(),
                        creationTime = "",
                        level = selectedLevel
                    )
                }

                else -> null
            }

        }

    }

}
