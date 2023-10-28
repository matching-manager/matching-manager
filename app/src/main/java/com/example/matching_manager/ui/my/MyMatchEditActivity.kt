package com.example.matching_manager.ui.my

import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.net.toUri
import androidx.lifecycle.Observer
import coil.load
import com.example.matching_manager.R
import com.example.matching_manager.databinding.MyMatchEditActivityBinding
import com.example.matching_manager.ui.match.MatchDataModel
import com.example.matching_manager.ui.match.MatchFragment
import com.example.matching_manager.ui.match.MatchSharedViewModel
import com.example.matching_manager.ui.match.MatchViewModel
import com.example.matching_manager.ui.match.MatchWritingActivity
import com.example.matching_manager.ui.my.bottomsheet.MyCalender
import com.example.matching_manager.ui.my.bottomsheet.MyNumber
import com.example.matching_manager.ui.my.bottomsheet.MyTime
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID

class MyMatchEditActivity : AppCompatActivity() {
    private lateinit var binding: MyMatchEditActivityBinding

    private val viewModel: MyViewModel by viewModels {
        MyMatchViewModelFactory()
    }
    private val sharedViewModel: MySharedViewModel by viewModels()

    private val reference: StorageReference = FirebaseStorage.getInstance().reference

    private var selectedGame: String? = null
    private var selectedGender: String? = null
    private var selectedLevel: String? = null

    private val data: MyMatchDataModel? by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(OBJECT_DATA, MyMatchDataModel::class.java)
        } else {
            intent.getParcelableExtra<MyMatchDataModel>(OBJECT_DATA)
        }
    }

    private var imageUri: Uri? = null

    companion object {
        const val OBJECT_DATA = "item_object"

        //바텀시트호출
        const val MATCH_NUMBER_BOTTOM_SHEET = "my_number_bottom_sheet"
        const val MATCH_TIME_BOTTOM_SHEET = "my_time_bottom_sheet"
        const val MATCH_CALENDER_BOTTOM_SHEET = "my_calender_bottom_sheet"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MyMatchEditActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
        initViewModel()
        setSpinner()

    }

    private fun setSpinner()  = with(binding) {
        // spinner adapter
        //종목 스피너
        val gameAdapter = ArrayAdapter.createFromResource(
            this@MyMatchEditActivity,
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

        //성별 스피너
        val genderAdapter = ArrayAdapter.createFromResource(
            this@MyMatchEditActivity,
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
            this@MyMatchEditActivity,
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

    private fun initViewModel()  = with(binding){
        with(viewModel) {
            event.observe(this@MyMatchEditActivity) {
                when (it) {
                    is MatchEvent.Finish -> {
                        finish()
                    }
                    is MatchEvent.Dismiss -> {
                    }
                }
            }
        }
        with(sharedViewModel) {
            number.observe(this@MyMatchEditActivity, Observer {
                Log.d("teamNumber", "activity = $it")
                tvTeamNumber1.text = it.toString()
                tvTeamNumber2.text = it.toString()

            })
            teamTime.observe(this@MyMatchEditActivity, Observer { (hour, minute, amPm) ->
                val time = String.format("%s %02d:%02d", amPm, hour, minute)
                Log.d("teamTime", "activity = $time")
                tvTime.text = time
            })
            calendar.observe(
                this@MyMatchEditActivity,
                Observer { (year, month, dayOfMonth, dayOfWeek) ->
                    val date =
                        String.format("%02d월 %02d일 %s", month, dayOfMonth, dayOfWeek)
                    Log.d("teamTime", "activity = $date")
                    tvMonthDate.text = date
                })
        }
    }

    private fun showCalenderPicker() {
        val bottomSheet = MyCalender()
        bottomSheet.show(supportFragmentManager, MATCH_CALENDER_BOTTOM_SHEET)
    }

    private fun showTimePicker() {
        val bottomSheet = MyTime()
        bottomSheet.show(supportFragmentManager, MATCH_TIME_BOTTOM_SHEET)
    }

    private fun showNumberPicker() {
        val bottomSheet = MyNumber()
        bottomSheet.show(supportFragmentManager, MATCH_NUMBER_BOTTOM_SHEET)
    }

    private fun initView() = with(binding) {

        val intent = Intent(this@MyMatchEditActivity, MyMatchMenuBottomSheet::class.java)
        setResult(RESULT_OK, intent)

        btnCancel.setOnClickListener {
            finish()
        }

        tvAddImage.setOnClickListener {
            val galleryIntent = Intent(Intent.ACTION_PICK)
            galleryIntent.type = "image/"
            imageResult.launch(galleryIntent)
        }

//        etTeamName.setText(data!!.teamName)
//        etSchedule.setText(data!!.schedule)
//        gameSpinner.setSelection(gameAdapter.getPosition(data!!.game))
//        etPlayerNum.setText(data!!.playerNum)
//        etMatchPlace.setText(data!!.matchPlace)
//        genderSpinner.setSelection(gameAdapter.getPosition(data!!.gender))
//        etEntryFee.setText(data!!.entryFee)
//        etDiscription.setText(data!!.description)
//        ivTeam.setImageURI(data!!.postImg.toUri())


        btnSubmit.setOnClickListener {
            val teamName = etTeamName?.text?.toString() ?: "" // Elvis 연산자를 사용하여 null일 경우 ""으로 초기화합니다.
            val game = (gameSpinner?.selectedItem?.toString() ?: "")
            val schedule = "${tvMonthDate?.text?.toString()} ${tvTime?.text?.toString()}"
            val playerNum = sharedViewModel.number.value ?: 0
            val matchPlace = etMatchPlace?.text?.toString() ?: ""
            val gender = genderSpinner?.selectedItem?.toString() ?: ""
            val level = levelSpinner?.selectedItem?.toString() ?: ""
            val entryFee = etEntryFee?.text?.toString()?.toInt() ?: 0
            val description = etDiscription?.text?.toString() ?: ""


            val editData = MyMatchDataModel(
                matchId = data!!.matchId,
                userId = data!!.userId,
                userNickname = data!!.userNickname,
                teamName = teamName,
                game = game,
                schedule = schedule,
                playerNum = playerNum,
                matchPlace = matchPlace,
                gender = gender,
                level = level,
                entryFee = entryFee,
                description = description,
                viewCount = data!!.viewCount,
                chatCount = data!!.chatCount,
                uploadTime = data!!.uploadTime
            )
            //예외처리 임시 주석처리
//            if (teamName.isBlank() || schedule.isBlank() || matchPlace.isBlank() || description.isBlank() || playerNum.toString()
//                    .isBlank() || entryFee.toString().isBlank()
//            ) {
//                // 선택되지 않은 값이 있을 때 토스트 메시지를 띄웁니다.
//                Toast.makeText(
//                    this@MyMatchEditActivity,
//                    "모든 항목을 입력해주세요",
//                    Toast.LENGTH_SHORT
//                ).show()
//                return@setOnClickListener
//            }

//            if (imageUri != null) {
//                uploadToFirebase(imageUri!!, data!!, dummyEditData)
//            } else {
//                Toast.makeText(this@MyMatchEditActivity, "사진을 선택해 주세요.", Toast.LENGTH_SHORT).show()
//            }
            uploadToFirebase(imageUri!!, data!!, editData)
        }
    }

    private val imageResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK && result.data != null) {
            imageUri = result.data?.data!!
            binding.ivTeam.load(imageUri)
        }
    }

    private fun getCurrentTime(): String {
        val currentTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

        return currentTime.format(formatter)
    }

    private fun uploadToFirebase(uri: Uri, data: MyMatchDataModel, newData: MyMatchDataModel) {
        val fileRef = reference.child("Match/${data.matchId}")

        fileRef.putFile(uri)
            .addOnSuccessListener {
                fileRef.downloadUrl
                    .addOnSuccessListener { uri ->
                        newData.postImg = uri.toString()
                        viewModel.editMatch(data, newData)

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