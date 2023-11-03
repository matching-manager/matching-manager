package com.example.matching_manager.ui.team

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import coil.load
import com.example.matching_manager.R
import com.example.matching_manager.databinding.TeamWritingActivityBinding
import com.example.matching_manager.ui.team.bottomsheet.TeamAge
import com.example.matching_manager.ui.team.bottomsheet.TeamCalender
import com.example.matching_manager.ui.team.bottomsheet.TeamNumber
import com.example.matching_manager.ui.team.bottomsheet.TeamTime
import com.example.matching_manager.ui.team.viewmodel.TeamSharedViewModel
import com.example.matching_manager.util.Spinners
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class TeamWritingActivity : AppCompatActivity() {
    private lateinit var binding: TeamWritingActivityBinding
    private var selectedGame: String? = null
    private var selectedArea: String? = null
    private var selectedGender: String? = null
    private var selectedLevel: String? = null
    private var selectedTime: String? = null
    private var imageUri: Uri? = null


    private val sheardViewModel: TeamSharedViewModel by viewModels()

    //진입타입 설정을 위함
    companion object {
        const val EXTRA_TEAM_ENTRY_TYPE = "extra_team_entry_type"
        const val EXTRA_TEAM_MODEL = "extra_team_model"
        const val TEAM_NUMBER_BOTTOM_SHEET = "team_number_bottom_sheet"
        const val TEAM_AGE_BOTTOM_SHEET = "team_age_bottom_sheet"
        const val TEAM_TIME_BOTTOM_SHEET = "team_time_bottom_sheet"
        const val TEAM_CALENDER_BOTTOM_SHEET = "team_calender_bottom_sheet"


        //용병모집
        fun newIntentForAddRecruit(
            context: Context,
            entryType: String,
        ) = Intent(context, TeamWritingActivity::class.java).apply {
            putExtra(EXTRA_TEAM_ENTRY_TYPE, entryType) // 타입을 전달
        }

        //용병신청
        fun newIntentForAddApplication(
            context: Context,
            entryType: String,
        ) = Intent(context, TeamWritingActivity::class.java).apply {
            putExtra(EXTRA_TEAM_ENTRY_TYPE, entryType) // 타입을 전달
        }
    }

    private val entryType by lazy {
        TeamAddType.from(intent.getStringExtra(EXTRA_TEAM_ENTRY_TYPE))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = TeamWritingActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d("TeamAddActivity", "in")
        setSpinner()
        initView()
        initViewModel()
    }

    private fun initViewModel() = with(binding) {
        with(sheardViewModel) {
            number.observe(this@TeamWritingActivity, Observer {
                Log.d("teamNumber", "activity = $it")
                teamNumber.text = it.toString()
            })
            age.observe(this@TeamWritingActivity, Observer {
                Log.d("teamAge", "activity = $it")
                teamAge.text = it.toString()
            })
            teamTime.observe(this@TeamWritingActivity, Observer { (hour, minute, amPm) ->
                val time = String.format("%s %02d:%02d", amPm, hour, minute)
                Log.d("teamTime", "activity = $time")
                tvTime.text = time
            })
            calendar.observe(
                this@TeamWritingActivity,
                Observer { (year, month, dayOfMonth, dayOfWeek) ->
                    val date =
                        String.format("%02d월 %02d일 %s", month, dayOfMonth, dayOfWeek)
                    Log.d("teamTime", "activity = $date")
                    tvMonthDate.text = date
                })
        }
    }

    private fun setSpinner() = with(binding) {
        // spinner adapter
        //종목 스피너
        val gameAdapter = Spinners.gameAdapter(context = this@TeamWritingActivity)
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

        //지역선택 스피너
        val cityAdapter = Spinners.cityAdapter(context = this@TeamWritingActivity)
        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        citySpinner.adapter = cityAdapter
        citySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long,
            ) {
                selectedArea = parent?.getItemAtPosition(position).toString()

                // 선택된 시/도에 따라 동작을 추가합니다.
                sigunguSpinner.visibility = (View.INVISIBLE)
                dongSpinner.visibility = (View.INVISIBLE)
                when (position) {
                    // 시/도 별로 동작을 구현합니다.
                    0 -> sigunguSpinner.adapter = null
                    else ->// 시/도가 다른 경우의 동작
                        // 예시로 setSigunguSpinnerAdapterItem 함수를 호출하는 코드를 추가합니다.
                        Spinners.positionToCityResource(position)
                            ?.let { setSigunguSpinnerAdapterItem(it) }

                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }

            fun setSigunguSpinnerAdapterItem(arrayResource: Int) {
                if (citySpinner.selectedItemPosition > 1) {
                    dongSpinner.adapter = null
                }
                val sigungnAdapter = ArrayAdapter(
                    this@TeamWritingActivity,
                    android.R.layout.simple_spinner_item,
                    resources.getStringArray(arrayResource)
                )
                sigungnAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                sigunguSpinner.adapter = sigungnAdapter
            }
        }

        sigunguSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long,
            ) {
                // 서울특별시 선택시
                sigunguSpinner.visibility = (View.VISIBLE)
                if (citySpinner.selectedItemPosition == 1 && sigunguSpinner.selectedItemPosition > -1) {
                    sigunguSpinner.visibility = (View.VISIBLE)
                    dongSpinner.visibility = (View.VISIBLE)
                    Spinners.positionToDongResource(position)
                        ?.let {
                            (setDongSpinnerAdapterItem(it))
                        }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }

            fun setDongSpinnerAdapterItem(arrayResource: Int) {
                val dongAdapter = ArrayAdapter(
                    this@TeamWritingActivity,
                    android.R.layout.simple_spinner_item,
                    resources.getStringArray(arrayResource)
                )
                dongAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                dongSpinner.adapter = dongAdapter
            }
        }

        //성별 스피너
        val genderAdapter = Spinners.genderAdapter(this@TeamWritingActivity)
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
        val levelAdapter = Spinners.levelAdapter(this@TeamWritingActivity)
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
        val timeAdapter = Spinners.timeAdapter(this@TeamWritingActivity)
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

        //date
        tvMonthDate.setOnClickListener {
            showCalenderPicker()
        }
        //time
        tvTime.setOnClickListener {
            showTimePicker()
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


    private fun initView() = with(binding) {
        //모집/신청에 따른 view배치 설정
        when (entryType) {
            TeamAddType.RECRUIT -> {
                cvRecruitTime.visibility = (View.VISIBLE)
                cvFee.visibility = (View.VISIBLE)
                cvTeamName.visibility = (View.VISIBLE)
            }

            else -> {
                cvApplicationTime.visibility = (View.VISIBLE)
                cvApplicationAge.visibility = (View.VISIBLE)
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

        fun formatTimeString(): String? {
            return SimpleDateFormat("yyyy.MM.dd", Locale.getDefault())
                .format(Date())
        }

        tvAddImage.setOnClickListener {
            val galleryIntent = Intent(Intent.ACTION_PICK)
            galleryIntent.type = "image/"
            imageResult.launch(galleryIntent)
        }


        btnSubmit.setOnClickListener {
            val selectedGame = "[" + gameSpinner.selectedItem.toString() + "]"
            val selectedArea =
                "[" + citySpinner.selectedItem.toString() + "/" + sigunguSpinner.selectedItem.toString() + "]"
            val selectedGender = genderSpinner.selectedItem.toString()
            val selectedLevel = levelSpinner.selectedItem.toString()
            val selectedApplicationTime = timeSpinner.selectedItem.toString()
            val selectedFee = tvFee.text.toString()
            val selectedTeamName = tvTeamName.text.toString()
            val setContent = etContent.text.toString()
            val selectedNumber = sheardViewModel.number.value ?: 0 // 기본값을 0으로 설정
            val selectedAge = sheardViewModel.age.value ?: 0 // 기본값을 0으로 설정
            val selectedDate = tvMonthDate.text.toString()
            val selectedTime = tvTime.text.toString()

            // 시간 포맷 변경 시작
            val formattedTime = formatTimeString().toString()


            val recruitment = getString(R.string.team_fragment_recruitment)
            val application = getString(R.string.team_fragment_application)
            val unfined = getString(R.string.undefined_test_value)


            val teamItem = when (entryType) {
                TeamAddType.RECRUIT -> {
                    formattedTime?.let {
                        TeamItem.RecruitmentItem(
                            type = recruitment, // 임의의 값으로 설정 (용병모집)
                            game = selectedGame,
                            area = selectedArea,//지역 설정하기 스피너 추가해야함
                            schedule = selectedDate + " " + selectedTime,//경기일정으로 되어있음 -> 달력바텀시트 만들어야함
                            teamProfile = 0,
                            playerNum = selectedNumber.toString() + "명",
                            pay = selectedFee,
                            teamName = selectedTeamName,
                            gender = selectedGender,
                            viewCount = 0,
                            chatCount = 0,
                            place = unfined,//경기장 추가해야함
                            nicname = unfined,
                            content = setContent,
                            creationTime = formattedTime,//시간
                            level = selectedLevel
                        )
                    }
                }

                TeamAddType.APPLICATION -> {
                    TeamItem.ApplicationItem(
                        type = application, // 임의의 값으로 설정 (용병신청)
                        game = selectedGame,
                        area = selectedArea,
                        schedule = selectedApplicationTime,
                        teamProfile = 0,
                        playerNum = selectedNumber.toString() + "명",
                        age = selectedAge.toString(),
                        gender = selectedGender,
                        viewCount = 0,
                        chatCount = 0,
                        nicname = unfined,
                        content = setContent,
                        creationTime = formattedTime,
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

    private val imageResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK && result.data != null) {
            imageUri = result.data?.data
            binding.ivImage.load(imageUri)
        }
    }

    private fun showCalenderPicker() {
        val bottomSheet = TeamCalender()
        bottomSheet.show(supportFragmentManager, TEAM_CALENDER_BOTTOM_SHEET)
    }

    private fun showTimePicker() {
        val bottomSheet = TeamTime()
        bottomSheet.show(supportFragmentManager, TEAM_TIME_BOTTOM_SHEET)
    }

    private fun showNumberPicker() {
        val bottomSheet = TeamNumber()
        bottomSheet.show(supportFragmentManager, TEAM_NUMBER_BOTTOM_SHEET)
    }

    private fun showAgePicker() {
        val bottomSheet = TeamAge()
        bottomSheet.show(supportFragmentManager, TEAM_AGE_BOTTOM_SHEET)
    }

}
