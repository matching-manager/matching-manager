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
        val gameAdapter = ArrayAdapter.createFromResource(
            this@TeamWritingActivity,
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

        //지역선택 스피너
        val arrayAdapter = ArrayAdapter.createFromResource(
            this@TeamWritingActivity,
            R.array.spinner_region,
            android.R.layout.simple_spinner_item
        )
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        citySpinner.adapter = arrayAdapter
        citySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long,
            ) {
                selectedArea = parent?.getItemAtPosition(position).toString()

//                // 선택된 시/도에 따라 동작을 추가합니다.
                sigunguSpinner.visibility = (View.INVISIBLE)
                dongSpinner.visibility = (View.INVISIBLE)
                when (position) {
                    // 시/도 별로 동작을 구현합니다.
                    0 -> sigunguSpinner.adapter = null
                    1 -> setSigunguSpinnerAdapterItem(R.array.spinner_region_seoul)
                    2 -> setSigunguSpinnerAdapterItem(R.array.spinner_region_busan)
                    3 -> setSigunguSpinnerAdapterItem(R.array.spinner_region_daegu)
                    4 -> setSigunguSpinnerAdapterItem(R.array.spinner_region_incheon)
                    5 -> setSigunguSpinnerAdapterItem(R.array.spinner_region_gwangju)
                    6 -> setSigunguSpinnerAdapterItem(R.array.spinner_region_daejeon)
                    7 -> setSigunguSpinnerAdapterItem(R.array.spinner_region_ulsan)
                    8 -> setSigunguSpinnerAdapterItem(R.array.spinner_region_sejong)
                    9 -> setSigunguSpinnerAdapterItem(R.array.spinner_region_gyeonggi)
                    10 -> setSigunguSpinnerAdapterItem(R.array.spinner_region_gangwon)
                    11 -> setSigunguSpinnerAdapterItem(R.array.spinner_region_chung_buk)
                    12 -> setSigunguSpinnerAdapterItem(R.array.spinner_region_chung_nam)
                    13 -> setSigunguSpinnerAdapterItem(R.array.spinner_region_jeon_buk)
                    14 -> setSigunguSpinnerAdapterItem(R.array.spinner_region_jeon_nam)
                    15 -> setSigunguSpinnerAdapterItem(R.array.spinner_region_gyeong_buk)
                    16 -> setSigunguSpinnerAdapterItem(R.array.spinner_region_gyeong_nam)
                    17 -> setSigunguSpinnerAdapterItem(R.array.spinner_region_jeju)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }

            fun setSigunguSpinnerAdapterItem(arrayResource: Int) {
                if (citySpinner.selectedItemPosition > 1) {
                    dongSpinner.adapter = null
                }
                val arrayAdapter1 = ArrayAdapter(
                    this@TeamWritingActivity,
                    android.R.layout.simple_spinner_item,
                    resources.getStringArray(arrayResource)
                )
                arrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                sigunguSpinner.adapter = arrayAdapter1
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
                    when (position) {
                        0 -> setDongSpinnerAdapterItem(R.array.spinner_region_seoul_gangnam)
                        1 -> setDongSpinnerAdapterItem(R.array.spinner_region_seoul_gangdong)
                        2 -> setDongSpinnerAdapterItem(R.array.spinner_region_seoul_gangbuk)
                        3 -> setDongSpinnerAdapterItem(R.array.spinner_region_seoul_gangseo)
                        4 -> setDongSpinnerAdapterItem(R.array.spinner_region_seoul_gwanak)
                        5 -> setDongSpinnerAdapterItem(R.array.spinner_region_seoul_gwangjin)
                        6 -> setDongSpinnerAdapterItem(R.array.spinner_region_seoul_guro)
                        7 -> setDongSpinnerAdapterItem(R.array.spinner_region_seoul_geumcheon)
                        8 -> setDongSpinnerAdapterItem(R.array.spinner_region_seoul_nowon)
                        9 -> setDongSpinnerAdapterItem(R.array.spinner_region_seoul_dobong)
                        10 -> setDongSpinnerAdapterItem(R.array.spinner_region_seoul_dongdaemun)
                        11 -> setDongSpinnerAdapterItem(R.array.spinner_region_seoul_dongjag)
                        12 -> setDongSpinnerAdapterItem(R.array.spinner_region_seoul_mapo)
                        13 -> setDongSpinnerAdapterItem(R.array.spinner_region_seoul_seodaemun)
                        14 -> setDongSpinnerAdapterItem(R.array.spinner_region_seoul_seocho)
                        15 -> setDongSpinnerAdapterItem(R.array.spinner_region_seoul_seongdong)
                        16 -> setDongSpinnerAdapterItem(R.array.spinner_region_seoul_seongbuk)
                        17 -> setDongSpinnerAdapterItem(R.array.spinner_region_seoul_songpa)
                        18 -> setDongSpinnerAdapterItem(R.array.spinner_region_seoul_yangcheon)
                        19 -> setDongSpinnerAdapterItem(R.array.spinner_region_seoul_yeongdeungpo)
                        20 -> setDongSpinnerAdapterItem(R.array.spinner_region_seoul_yongsan)
                        21 -> setDongSpinnerAdapterItem(R.array.spinner_region_seoul_eunpyeong)
                        22 -> setDongSpinnerAdapterItem(R.array.spinner_region_seoul_jongno)
                        23 -> setDongSpinnerAdapterItem(R.array.spinner_region_seoul_jung)
                        24 -> setDongSpinnerAdapterItem(R.array.spinner_region_seoul_jungnanggu)
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }

            fun setDongSpinnerAdapterItem(arrayResource: Int) {
                val arrayAdapter = ArrayAdapter(
                    this@TeamWritingActivity,
                    android.R.layout.simple_spinner_item,
                    resources.getStringArray(arrayResource)
                )
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                dongSpinner.adapter = arrayAdapter
            }
        }

        //성별 스피너
        val genderAdapter = ArrayAdapter.createFromResource(
            this@TeamWritingActivity,
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
            this@TeamWritingActivity,
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
            this@TeamWritingActivity,
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
            val currentDate =
                java.text.SimpleDateFormat("yyyy.MM.dd", java.util.Locale.getDefault())
                    .format(java.util.Date())
            return currentDate
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

//            //예외처리
//            if (selectedGame.isBlank() ||
//                selectedArea.isBlank() ||
//                selectedGender.isBlank() ||
//                selectedLevel.isBlank() ||
//                selectedApplicationTime.isBlank() ||
//                selectedFee.isBlank() ||
//                selectedTeamName.isBlank() ||
//                setContent.isBlank() ||
//                selectedDate.isBlank() ||
//                selectedTime.isBlank()
//            ) {
//                // 선택되지 않은 값이 있을 때 토스트 메시지를 띄웁니다.
//                Toast.makeText(
//                    this@TeamWritingActivity, "비어있는 칸이 있습니다. 값을 입력해주세요", Toast.LENGTH_SHORT).show()
//                return@setOnClickListener
//            }

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

}
