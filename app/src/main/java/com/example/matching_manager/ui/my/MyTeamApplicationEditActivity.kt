package com.example.matching_manager.ui.my

import android.content.Context
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
import androidx.lifecycle.Observer
import coil.load
import com.example.matching_manager.R
import com.example.matching_manager.databinding.MyTeamApplicationEditActivityBinding
import com.example.matching_manager.ui.team.TeamItem
import com.example.matching_manager.ui.team.TeamWritingActivity
import com.example.matching_manager.ui.team.bottomsheet.TeamAge
import com.example.matching_manager.ui.team.bottomsheet.TeamNumber
import com.example.matching_manager.ui.team.viewmodel.TeamSharedViewModel
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class MyTeamApplicationEditActivity : AppCompatActivity() {
    private lateinit var binding : MyTeamApplicationEditActivityBinding

    private var selectedGame: String? = null
    private var selectedArea: String? = null
    private var selectedGender: String? = null
    private var selectedLevel: String? = null
    private var selectedTime: String? = null
    private var imageUri: Uri? = null


    private val sharedViewModel: TeamSharedViewModel by viewModels()

    private val viewModel: MyViewModel by viewModels {
        MyMatchViewModelFactory()
    }

    private val reference: StorageReference = FirebaseStorage.getInstance().reference

    private val data: TeamItem.ApplicationItem? by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(
                OBJECT_DATA,
                TeamItem.ApplicationItem::class.java
            )
        } else {
            intent.getParcelableExtra<TeamItem.ApplicationItem>(OBJECT_DATA)
        }
    }

    private val imageResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK && result.data != null) {
            imageUri = result.data?.data!!
            binding.ivImage.load(imageUri)
        }
    }

    companion object {
        const val OBJECT_DATA = "item_object"

        fun editIntent(
            context: Context, item: TeamItem.ApplicationItem
        ): Intent {
            val intent = Intent(context, MyTeamApplicationEditActivity::class.java)
            intent.putExtra(OBJECT_DATA, item)
            return intent
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MyTeamApplicationEditActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
        initViewModel()
        setSpinner()
    }

    private fun initView() = with(binding) {
        val intent = Intent(this@MyTeamApplicationEditActivity, MyMatchMenuBottomSheet::class.java)
        setResult(RESULT_OK, intent)



        btnCancel.setOnClickListener {
            finish()
        }

        tvAddImage.setOnClickListener {
            val galleryIntent = Intent(Intent.ACTION_PICK)
            galleryIntent.type = "image/"
            imageResult.launch(galleryIntent)
        }

        btnSubmit.setOnClickListener {
            val selectedGame = gameSpinner.selectedItem.toString()
            val selectedArea = citySpinner.selectedItem.toString() + "/" + sigunguSpinner.selectedItem.toString()
            val selectedGender = genderSpinner.selectedItem.toString()
            val selectedLevel = levelSpinner.selectedItem.toString()
            val selectedApplicationTime = timeSpinner.selectedItem.toString()
            val setContent = etContent.text.toString()
            val selectedNumber = sharedViewModel.number.value ?: 0 // 기본값을 0으로 설정
            val selectedAge = sharedViewModel.age.value ?: 0 // 기본값을 0으로 설정

            val editTeam = TeamItem.ApplicationItem(
                description = setContent,
                gender = selectedGender,
                level = selectedLevel,
                playerNum = selectedNumber,
                game = selectedGame,
                area = selectedArea,
                schedule = selectedApplicationTime,
                age = selectedAge
            )

            uploadToFirebase(imageUri, data!!, editTeam)
        }

    }

    private fun initViewModel() = with(binding) {
        with(viewModel) {
            event.observe(this@MyTeamApplicationEditActivity) {
                when (it) {
                    is MyEvent.Finish -> {
                        finish()
                    }
                    is MyEvent.Dismiss -> {
                    }
                }
            }
        }

        with(sharedViewModel) {
            number.observe(this@MyTeamApplicationEditActivity, Observer {
                Log.d("teamNumber", "activity = $it")
                teamNumber.text = it.toString()
            })
            age.observe(this@MyTeamApplicationEditActivity, Observer {
                Log.d("teamAge", "activity = $it")
                teamAge.text = it.toString()
            })
        }

    }

    private fun setSpinner() = with(binding) {
        // spinner adapter
        //종목 스피너
        val gameAdapter = ArrayAdapter.createFromResource(
            this@MyTeamApplicationEditActivity,
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
            this@MyTeamApplicationEditActivity,
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
                    this@MyTeamApplicationEditActivity,
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
                    this@MyTeamApplicationEditActivity,
                    android.R.layout.simple_spinner_item,
                    resources.getStringArray(arrayResource)
                )
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                dongSpinner.adapter = arrayAdapter
            }
        }

        //성별 스피너
        val genderAdapter = ArrayAdapter.createFromResource(
            this@MyTeamApplicationEditActivity,
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
            this@MyTeamApplicationEditActivity,
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
            this@MyTeamApplicationEditActivity,
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
        bottomSheet.show(supportFragmentManager, TeamWritingActivity.TEAM_NUMBER_BOTTOM_SHEET)
    }

    private fun showAgePicker() {
        val bottomSheet = TeamAge()
        bottomSheet.show(supportFragmentManager, TeamWritingActivity.TEAM_AGE_BOTTOM_SHEET)
    }

    private fun uploadToFirebase(uri: Uri?, data: TeamItem.ApplicationItem, newData: TeamItem.ApplicationItem) {
        val fileRef = reference.child("Match/${data.teamId}")

        if (uri != null) {
            fileRef.putFile(uri)
                .addOnSuccessListener {
                    fileRef.downloadUrl
                        .addOnSuccessListener { uri ->
                            newData.postImg = uri.toString()
                            viewModel.editApplication(data, newData)

                            binding.progressBar.visibility = View.INVISIBLE

                            Toast.makeText(this, "게시글이 수정되었습니다.", Toast.LENGTH_SHORT).show()

                        }
                }
                .addOnProgressListener { snapshot ->
                    binding.progressBar.visibility = View.VISIBLE
                }
                .addOnFailureListener { e ->
                    binding.progressBar.visibility = View.INVISIBLE
                    Toast.makeText(this, "게시글 수정을 실패하였습니다. 다시 시도해 주세요.", Toast.LENGTH_SHORT).show()
                }
        }
        else {
            binding.progressBar.visibility = View.VISIBLE
            viewModel.editApplication(data, newData)
            binding.progressBar.visibility = View.INVISIBLE
            Toast.makeText(this, "게시글이 수정되었습니다.", Toast.LENGTH_SHORT).show()
        }
    }
}