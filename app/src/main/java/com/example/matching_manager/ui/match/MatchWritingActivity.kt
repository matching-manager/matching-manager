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
import com.example.matching_manager.ui.team.view.TeamSharedViewModel
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
    private val sheardViewModel: MatchSharedViewModel by viewModels()

    private val reference: StorageReference = FirebaseStorage.getInstance().reference
    private var imageUri: Uri? = null

    companion object {
        const val ID_DATA = "item_userId"

        const val REVIEW_MIN_LENGTH = 10

        // 갤러리 권한 요청
        const val REQ_GALLERY = 1

        // API 호출시 Parameter key값
        const val PARAM_KEY_IMAGE = "image"
        const val PARAM_KEY_PRODUCT_ID = "product_id"
        const val PARAM_KEY_REVIEW = "review_content"
        const val PARAM_KEY_RATING = "rating"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MatchWritingActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
        initViewModel()

    }

    private fun initViewModel() = with(binding) {
        with(viewModel){
            event.observe(this@MatchWritingActivity) {
                when (it) {
                    is MatchEvent.Finish -> {
                        finish()
                    }

                    else -> {
                    }
                }
            }
        }
        with(sheardViewModel) {
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

    @SuppressLint("SuspiciousIndentation")
    private fun initView() = with(binding) {

        //back button
        btnCancel.setOnClickListener {
            finish() // 현재 Activity 종료
        }

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
                id: Long,
            ) {
                selectedGame = parent?.getItemAtPosition(position).toString()
                Log.d("game", "${selectedGame}")
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
                id: Long,
            ) {
                selectedGender = parent?.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        tvAddImage.setOnClickListener {
            val galleryIntent = Intent(Intent.ACTION_PICK)
            galleryIntent.type = "image/"
            imageResult.launch(galleryIntent)
        }


        val matchId = UUID.randomUUID().toString()
        val teamName = etTeamName.text.toString()
        val game = selectedGame
        val date = tvMonthDate.text.toString()
        val time = tvTime.text.toString()
        val playerNum1 = tvTeamNumber1.text.toString()
        val playerNum2 = tvTeamNumber2.text.toString()
        val matchPlace = etMatchPlace.text.toString()
        val gender = selectedGender
        val entryFee = etEntryFee.text.toString()
        val description = etDiscription.text.toString()
        val uploadTime = getCurrentTime()


        btnSubmit.setOnClickListener {
            Log.d("gameSave", "${game}")
            //테스트용 객체
            val dummyMatch = MatchDataModel(
                matchId = matchId,
                schedule = "$date $time",
                game = selectedGame,
                uploadTime = uploadTime
            )
            //실제 객체
//            val match = MatchDataModel(matchId = matchId,teamName = teamName, game = game, schedule = schedule, matchPlace = matchPlace, playerNum = playerNum.toInt(), entryFee = entryFee.toInt(), description = description, gender = gender, viewCount = 0, chatCount = 0, uploadTime = uploadTime)


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

            if (imageUri != null) {
                uploadToFirebase(imageUri!!, dummyMatch)
            } else {
                Toast.makeText(this@MatchWritingActivity, "사진을 선택해 주세요.", Toast.LENGTH_SHORT).show()
            }
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