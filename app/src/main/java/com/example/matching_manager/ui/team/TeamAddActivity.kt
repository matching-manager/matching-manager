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
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.matching_manager.R
import com.example.matching_manager.databinding.TeamAddActivityBinding
import com.example.matching_manager.ui.team.bottomsheet.TeamAge
import com.example.matching_manager.ui.team.bottomsheet.TeamNumber
import com.example.matching_manager.ui.team.view.TeamSharedViewModel


class TeamAddActivity : AppCompatActivity() {
    private lateinit var binding: TeamAddActivityBinding
    private var selectedGame: String? = null
    private var selectedArea: String? = null
    private var selectedGender: String? = null
    private var selectedLevel: String? = null
    private var selectedTime: String? = null


    private val viewModel: TeamSharedViewModel by viewModels()

    //진입타입 설정을 위함
    companion object {
        const val EXTRA_TEAM_ENTRY_TYPE = "extra_team_entry_type"
        const val EXTRA_TEAM_MODEL = "extra_team_model"
        const val TEAM_NUMBER_BOTTOM_SHEET = "team_number_bottom_sheet"
        const val TEAM_AGE_BOTTOM_SHEET = "team_age_bottom_sheet"


        //용병모집
        fun newIntentForAddRecruit(
            context: Context,
            entryType: String,
        ) = Intent(context, TeamAddActivity::class.java).apply {
            putExtra(EXTRA_TEAM_ENTRY_TYPE, entryType) // 타입을 전달
        }

        //용병신청
        fun newIntentForAddApplication(
            context: Context,
            entryType: String,
        ) = Intent(context, TeamAddActivity::class.java).apply {
            putExtra(EXTRA_TEAM_ENTRY_TYPE, entryType) // 타입을 전달
        }
    }

    private val entryType by lazy {
        TeamAddType.from(intent.getStringExtra(EXTRA_TEAM_ENTRY_TYPE))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = TeamAddActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d("TeamAddActivity", "in")
        setSpinner()
        initView()
        initViewModel()
    }

    private fun initViewModel() {
        with(viewModel) {
            number.observe(this@TeamAddActivity, Observer {
                Log.d("teamNumber", "activity = $it")
                binding.teamNumber.text = it.toString()
            })
            age.observe(this@TeamAddActivity, Observer {
                Log.d("teamAge", "activity = $it")
                binding.teamAge.text = it.toString()
            })
        }
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
        teamAge.setOnClickListener {
            showAgePicker()
        }

    }

    private fun showNumberPicker() {
        val bottomSheet = TeamNumber()
        bottomSheet.show(supportFragmentManager, TEAM_NUMBER_BOTTOM_SHEET)
    }

    private fun showAgePicker() {
        val bottomSheet = TeamAge()
        bottomSheet.show(supportFragmentManager, TEAM_AGE_BOTTOM_SHEET)
    }


    private fun initView() = with(binding) {
        //모집/신청에 따른 view배치 설정
        when (entryType) {
            TeamAddType.RECRUIT -> {
                cvRecruitTime.visibility=(View.VISIBLE)
                cvFee.visibility=(View.VISIBLE)
                cvTeamName.visibility=(View.VISIBLE)
            }

            else -> {
                cvApplicationTime.visibility=(View.VISIBLE)
                cvApplicationAge.visibility=(View.VISIBLE)
            }
        }

        //인포 이름 변경
        tvDialogInfo.setText(
            when (entryType) {
                TeamAddType.RECRUIT -> {
                    R.string.team_add_activity_recruit
                }

                else -> R.string.team_add_activity_application
            }
        )
        //인포 이름 변경
        tvDialogInfo.setText(
            when (entryType) {
                TeamAddType.RECRUIT -> {
                    R.string.team_add_activity_recruit
                }

                else -> R.string.team_add_activity_application
            }
        )

        //back button
        btnCancel.setOnClickListener {
            finish() // 현재 Activity 종료
        }

        //인포 이름 변경
        tvDialogInfo.setText(
            when (entryType) {
                TeamAddType.RECRUIT -> {
                    R.string.team_add_activity_recruit
                }

                else -> R.string.team_add_activity_application
            }
        )

        btnSubmit.setOnClickListener {
            val selectedGame = gameSpinner.selectedItem.toString()
            val selectedArea = areaSpinner.selectedItem.toString()
            val selectedGender = genderSpinner.selectedItem.toString()
            val selectedLevel = levelSpinner.selectedItem.toString()
            val selectedApplicationTime = timeSpinner.selectedItem.toString()
            val selectedFee=tvFee.text.toString()
            val selectedTeamName=tvTeamName.text.toString()
            val setContent = etContent.text.toString()
            val selectedNumber = viewModel.number.value ?: 0 // 기본값을 0으로 설정
            val selectedAge = viewModel.age.value ?: 0 // 기본값을 0으로 설정
            val recruitment = getString(R.string.team_fragment_recruitment)
            val application = getString(R.string.team_fragment_application)
            val unfined = getString(R.string.undefined_test_value)


            val teamItem = when (entryType) {
                TeamAddType.RECRUIT -> {
                    TeamItem.RecruitmentItem(
                        type = recruitment, // 임의의 값으로 설정 (용병모집)
                        game = selectedGame,
                        area = selectedArea,//지역 설정하기 스피너 추가해야함
                        schedule = unfined,//경기일정으로 되어있음 -> 달력바텀시트 만들어야함
                        teamProfile = 0,
                        playerNum = selectedNumber.toString(),
                        pay = selectedFee,
                        teamName = selectedTeamName,
                        gender = selectedGender,
                        viewCount = 0,
                        chatCount = 0,
                        place = unfined,//경기장 추가해야함
                        nicname = unfined,
                        content = setContent,
                        creationTime = unfined,
                        level = selectedLevel
                    )
                }

                TeamAddType.APPLICATION -> {
                    TeamItem.ApplicationItem(
                        type = application, // 임의의 값으로 설정 (용병신청)
                        game = selectedGame,
                        area = selectedArea,
                        schedule = selectedApplicationTime,
                        teamProfile = 0,
                        playerNum = selectedNumber.toString(),
                        age = selectedAge.toString(),
                        gender = selectedGender,
                        viewCount = 0,
                        chatCount = 0,
                        nicname = unfined,
                        content = setContent,
                        creationTime = unfined,
                        level = selectedLevel
                    )
                }

                else -> null
            }

            val intent = Intent().apply {
                putExtra(EXTRA_TEAM_ENTRY_TYPE, entryType?.name)
                putExtra(EXTRA_TEAM_MODEL, teamItem)
            }
            setResult(Activity.RESULT_OK, intent)
            finish()

        }

    }

}
