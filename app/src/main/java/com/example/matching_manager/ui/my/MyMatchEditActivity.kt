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
import coil.load
import com.example.matching_manager.R
import com.example.matching_manager.databinding.MyMatchEditActivityBinding
import com.example.matching_manager.ui.match.MatchDataModel
import com.example.matching_manager.ui.match.MatchFragment
import com.example.matching_manager.ui.match.MatchViewModel
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

    private val reference: StorageReference = FirebaseStorage.getInstance().reference

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

        val intent = Intent(this@MyMatchEditActivity, MyMatchMenuBottomSheet::class.java)
        setResult(RESULT_OK, intent)

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

        val teamName = etTeamName.text.toString()
        val game = selectedGame
        val date = tvMonthDate.text.toString()
        val time = tvTime.text.toString()
        val schedule = "$date +$time"
        val playerNum = "$playerNum 1VS$playerNum2"
        val playerNum1 = tvTeamNumber1.text.toString()
        val playerNum2 = tvTeamNumber2.text.toString()
        val matchPlace = etMatchPlace.text.toString()
        val gender = selectedGender
        val entryFee = etEntryFee.text.toString()
        val description = etDiscription.text.toString()
        val uploadTime = getCurrentTime()

        btnSubmit.setOnClickListener {
            val dummyEditData = MyMatchDataModel(
                matchId = etTeamName.text.toString(),
                schedule = schedule
                uploadTime = uploadTime
            )
            val editData = MyMatchDataModel(
                matchId = data!!.matchId,
                teamName = teamName,
                game = game,
                schedule = schedule,
                matchPlace = matchPlace,
                playerNum = playerNum.toInt(),
                entryFee = entryFee.toInt(),
                description = description,
                gender = gender,
                viewCount = 0,
                chatCount = 0,
                uploadTime = uploadTime
            )

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

            if (imageUri != null) {
                uploadToFirebase(imageUri!!, data!!, dummyEditData)
            } else {
                Toast.makeText(this@MyMatchEditActivity, "사진을 선택해 주세요.", Toast.LENGTH_SHORT).show()
            }
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