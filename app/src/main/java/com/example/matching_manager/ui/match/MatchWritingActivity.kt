package com.example.matching_manager.ui.match

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import coil.load
import com.example.matching_manager.R
import com.example.matching_manager.databinding.MatchWritingActivityBinding
import com.example.matching_manager.ui.match.bottomsheet.MatchCalender
import com.example.matching_manager.ui.match.bottomsheet.MatchNumber
import com.example.matching_manager.ui.match.bottomsheet.MatchTime
import com.example.matching_manager.util.Spinners
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID

class MatchWritingActivity : AppCompatActivity() {

    private lateinit var binding: MatchWritingActivityBinding

    private val viewModel: MatchViewModel by viewModels {
        MatchViewModelFactory()
    }
    private val sharedViewModel: MatchSharedViewModel by viewModels()

    private val reference: StorageReference = FirebaseStorage.getInstance().reference
    private var imageUri: Uri? = null

    private var selectedGame: String? = null
    private var selectedGender: String? = null
    private var selectedLevel: String? = null
    private var selectedArea : String? = null

    companion object {
        const val ID_DATA = "item_userId"

        //바텀시트호출
        const val MATCH_NUMBER_BOTTOM_SHEET = "match_number_bottom_sheet"
        const val MATCH_TIME_BOTTOM_SHEET = "match_time_bottom_sheet"
        const val MATCH_CALENDER_BOTTOM_SHEET = "match_calender_bottom_sheet"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MatchWritingActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
        initViewModel()
        setSpinner()
    }

    private fun setSpinner() = with(binding) {
        // spinner adapter
        //종목 스피너
        val gameAdapter = ArrayAdapter.createFromResource(
            this@MatchWritingActivity,
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
        val cityAdapter = Spinners.cityAdapter(context = this@MatchWritingActivity)
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
                    this@MatchWritingActivity,
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
                    this@MatchWritingActivity,
                    android.R.layout.simple_spinner_item,
                    resources.getStringArray(arrayResource)
                )
                dongAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                dongSpinner.adapter = dongAdapter
            }
        }

        //성별 스피너
        val genderAdapter = ArrayAdapter.createFromResource(
            this@MatchWritingActivity,
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
            this@MatchWritingActivity,
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

        //date
        tvMonthDate.setOnClickListener {
            showCalenderPicker()
        }
        //time
        tvTime.setOnClickListener {
            showTimePicker()
        }
        //number
        val clickListener = View.OnClickListener {
            showNumberPicker()
        }
        tvTeamNumber1.setOnClickListener(clickListener)
        tvTeamNumber2.setOnClickListener(clickListener)
    }

    private fun initViewModel() = with(binding) {
        with(viewModel) {
            event.observe(this@MatchWritingActivity) {
                when (it) {
                    is MatchViewModel.MatchEvent.Finish -> {
                        finish()
                    }

                    else -> {
                    }
                }
            }
        }
        with(sharedViewModel) {
            number.observe(this@MatchWritingActivity, Observer {
                Log.d("teamNumber", "activity = $it")
                tvTeamNumber1.text = it.toString()
                tvTeamNumber2.text = it.toString()

            })
            teamTime.observe(this@MatchWritingActivity, Observer { (hour, minute, amPm) ->
                val time = String.format("%s %02d:%02d", amPm, hour, minute)
                Log.d("teamTime", "activity = $time")
                tvTime.text = time
            })
            calendar.observe(
                this@MatchWritingActivity,
                Observer { (year, month, dayOfMonth, dayOfWeek) ->
                    val date =
                        String.format("%02d월 %02d일 %s", month, dayOfMonth, dayOfWeek)
                    Log.d("teamTime", "activity = $date")
                    tvMonthDate.text = date
                })
        }

    }

    private fun showCalenderPicker() {
        val bottomSheet = MatchCalender()
        bottomSheet.show(supportFragmentManager, MATCH_CALENDER_BOTTOM_SHEET)
    }

    private fun showTimePicker() {
        val bottomSheet = MatchTime()
        bottomSheet.show(supportFragmentManager, MATCH_TIME_BOTTOM_SHEET)
    }

    private fun showNumberPicker() {
        val bottomSheet = MatchNumber()
        bottomSheet.show(supportFragmentManager, MATCH_NUMBER_BOTTOM_SHEET)
    }


    @SuppressLint("SuspiciousIndentation")
    private fun initView() = with(binding) {

        //matchFragment에서 가져오는 userID
        val userId = intent.getStringExtra(ID_DATA)
        Log.d("userId", "${userId}")

        //back button
        btnCancel.setOnClickListener {
            finish() // 현재 Activity 종료
        }

        btnSubmit.setOnClickListener {
            val matchId = UUID.randomUUID().toString()
            val teamName = etTeamName?.text?.toString() ?: "" // Elvis 연산자를 사용하여 null일 경우 ""으로 초기화합니다.
            val game = (gameSpinner?.selectedItem?.toString() ?: "")
            val schedule = "${tvMonthDate?.text?.toString()} ${tvTime?.text?.toString()}"
            val playerNum = sharedViewModel.number.value ?: 0
            val matchPlace = citySpinner.selectedItem.toString() + "/" + sigunguSpinner.selectedItem.toString()
            val gender = genderSpinner?.selectedItem?.toString() ?: ""
            val level = levelSpinner?.selectedItem?.toString() ?: ""
            val entryFee = etEntryFee?.text?.toString()?.toInt() ?: 0
            val description = etDiscription?.text?.toString() ?: ""
            val uploadTime = getCurrentTime()

            //현재 유저 아이디 및 유저 닉네임은 임시, 나중에 로그인 기능 구현되면 추가해야함
            val match = MatchDataModel(
                matchId = matchId,
                teamName = teamName,
                game = game,
                schedule = schedule,
                playerNum = playerNum,
                matchPlace = matchPlace,
                gender = gender,
                level = level,
                entryFee = entryFee,
                description = description,
                viewCount = 0,
                chatCount = 0,
                uploadTime = uploadTime
            )

            val intent = Intent(this@MatchWritingActivity, MatchFragment::class.java)
            setResult(RESULT_OK, intent)

//            if (teamName.isBlank() || schedule.isBlank() || matchPlace.isBlank() || description.isBlank() || selectedGame.isBlank() || playerNum.isBlank()  || entryFee.isBlank()) {
//                // 선택되지 않은 값이 있을 때 토스트 메시지를 띄웁니다.
//                Toast.makeText(
//                    this@MatchWritingActivity,
//                    "모든 항목을 입력해주세요",
//                    Toast.LENGTH_SHORT
//                ).show()
//                return@setOnClickListener
//            }
            //사진 예외처리
//            if (imageUri != null) {
//                uploadToFirebase(imageUri!!, match)
//            } else {
//                Toast.makeText(this@MatchWritingActivity, "사진을 선택해 주세요.", Toast.LENGTH_SHORT).show()
//            }
            //현재는 예외처리는 전부 제외했기 때문에 전부 작성하고 글 올려야합니다!!
            uploadToFirebase(imageUri!!, match)
        }

        tvAddImage.setOnClickListener {
            val galleryIntent = Intent(Intent.ACTION_PICK)
            galleryIntent.type = "image/"
            imageResult.launch(galleryIntent)
        }
    }

    private fun getCurrentTime(): String {
        val currentTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

        return currentTime.format(formatter)
    }

    private val imageResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK && result.data != null) {
            imageUri = result.data?.data
            binding.ivTeam.load(imageUri)
        }
    }

    private fun uploadToFirebase(uri: Uri, data: MatchDataModel) {
        val fileRef = reference.child("Match/${data.matchId}")

        fileRef.putFile(uri)
            .addOnSuccessListener {
                fileRef.downloadUrl
                    .addOnSuccessListener { uri ->
                        data.postImg = uri.toString()
                        viewModel.addMatch(data)

                        binding.progressBar.visibility = View.INVISIBLE

                        Toast.makeText(this, "매치가 등록되었습니다.", Toast.LENGTH_SHORT).show()

                    }
            }
            .addOnProgressListener { snapshot ->
                binding.progressBar.visibility = View.VISIBLE
            }
            .addOnFailureListener { e ->
                binding.progressBar.visibility = View.INVISIBLE
                Toast.makeText(this, "매치 등록을 실패하였습니다. 다시 시도해 주세요.", Toast.LENGTH_SHORT).show()
            }
    }
}