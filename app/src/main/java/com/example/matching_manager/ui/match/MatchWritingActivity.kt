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
import coil.load
import com.example.matching_manager.R
import com.example.matching_manager.databinding.MatchWritingActivityBinding
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

    private val reference: StorageReference = FirebaseStorage.getInstance().reference
    private var imageUri: Uri? = null

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
                id: Long,
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
                id: Long,
            ) {
                selectedGender = gameAdapter.getItem(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        tvAddImage.setOnClickListener {
            val galleryIntent = Intent(Intent.ACTION_GET_CONTENT)
            galleryIntent.type = "image/"
            activityResult.launch(galleryIntent)
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
            //테스트용 객체
            val dummyMatch = MatchDataModel(
                matchId = matchId,
                schedule = etSchedule.text.toString(),
                uploadTime = uploadTime
            )
            //실제 객체
            val match = MatchDataModel(
                matchId = matchId,
                teamName = teamName,
                game = game,
                schedule = schedule,
                matchPlace = matchPlace,
                playerNum = playerNum,
                entryFee = entryFee,
                description = description,
                gender = gender,
                viewCount = 0,
                chatCount = 0,
                uploadTime = uploadTime
            )


            val intent = Intent(this@MatchWritingActivity, MatchFragment::class.java)
            setResult(RESULT_OK, intent)

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

    private val activityResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK && result.data != null) {
            imageUri = result.data?.data
            binding.ivTeam.load(imageUri)
        }
    }

    // 파이어베이스 이미지 업로드
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