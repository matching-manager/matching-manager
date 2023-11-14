package com.link_up.matching_manager.ui.my.team

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
import com.link_up.matching_manager.R
import com.link_up.matching_manager.databinding.MyTeamApplicationEditActivityBinding
import com.link_up.matching_manager.ui.my.my.MyEvent
import com.link_up.matching_manager.ui.my.my.MyPostViewModel
import com.link_up.matching_manager.ui.my.match.MyMatchMenuBottomSheet
import com.link_up.matching_manager.ui.my.my.MyPostViewModelFactory
import com.link_up.matching_manager.ui.team.TeamItem
import com.link_up.matching_manager.ui.team.TeamWritingActivity
import com.link_up.matching_manager.ui.team.bottomsheet.TeamAgeBottomSheet
import com.link_up.matching_manager.ui.team.bottomsheet.TeamNumberBottomSheet
import com.link_up.matching_manager.ui.team.view_model.TeamSharedViewModel
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.link_up.matching_manager.util.Spinners

class MyTeamApplicationEditActivity : AppCompatActivity() {
    private lateinit var binding: MyTeamApplicationEditActivityBinding

    private var selectedGame: String? = null
    private var selectedArea: String? = null
    private var selectedGender: String? = null
    private var selectedLevel: String? = null
    private var selectedTime: String? = null
    private var imageUri: Uri? = null


    private val sharedViewModel: TeamSharedViewModel by viewModels()

    private val viewModel: MyPostViewModel by viewModels {
        MyPostViewModelFactory()
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
            binding.btnCancelImage.visibility = View.VISIBLE
        }
    }

    companion object {
        const val OBJECT_DATA = "item_object"

        fun editIntent(
            context: Context, item: TeamItem.ApplicationItem,
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
        btnCancel.title = R.string.team_add_activity_application.toString()
        etContent.setText(data!!.description)

        if (data!!.postImg != "") {
            ivImage.load(data!!.postImg)
            btnCancelImage.visibility = View.VISIBLE
        }

        btnCancelImage.setOnClickListener {
            imageUri = null
            ivImage.setImageDrawable(null)
            btnCancelImage.visibility = View.INVISIBLE
        }

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
            val selectedArea =
                citySpinner.selectedItem.toString() + "/" + sigunguSpinner.selectedItem.toString()
            val selectedGender = genderSpinner.selectedItem.toString()
            val selectedLevel = levelSpinner.selectedItem.toString()
            val selectedApplicationTime = timeSpinner.selectedItem.toString()
            val setContent = etContent.text.toString()
            val selectedNumber = teamNumber.text.toString().toIntOrNull() ?: 0 // 기본값을 0으로 설정
            val selectedAge = teamAge.text.toString().toIntOrNull() ?: 0 // 기본값을 0으로 설정

            val teamNumberText = teamNumber.text?.toString()
            val teamAgeText = teamAge.text?.toString()
            when {
                selectedGame.contains("선택") -> {
                    showToast("종목을 선택해 주세요")
                    return@setOnClickListener
                }

                selectedArea.contains("선택") -> {
                    showToast("지역을 선택해 주세요")
                    return@setOnClickListener
                }

                selectedApplicationTime.contains("선택") -> {
                    showToast("일정을 선택해 주세요")
                    return@setOnClickListener
                }

                teamNumberText.isNullOrEmpty() -> {
                    showToast("인원을 선택해 주세요")
                    return@setOnClickListener
                }

                teamAgeText.isNullOrEmpty() -> {
                    showToast("나이을 선택해 주세요")
                    return@setOnClickListener
                }

                selectedGender.contains("선택") -> {
                    showToast("성별을 선택해 주세요")
                    return@setOnClickListener
                }

                selectedLevel.contains("선택") -> {
                    showToast("실력을 선택해 주세요")
                    return@setOnClickListener
                }

                setContent.isBlank() -> {
                    setContent.let {
                        if (it.isBlank()) {
                            showToast("내용을 입력해 주세요")
                            return@setOnClickListener
                        } else if (setContent.length < 10) {
                            showToast("내용은 최소 10글자 이상 입력해 주세요")
                            return@setOnClickListener
                        }
                    }
                }

                else -> {}
            }

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
        //기존 데이터 선택값으로 시작
        gameSpinner.setSelection(gameAdapter.getPosition(data!!.game))

        //기존 지역 데이터에서 시, 구 분리
        val areaParts = data!!.area.split("/")
        val city = areaParts[0]
        val gu = areaParts[1]

        //지역선택 스피너
        val cityAdapter = Spinners.cityAdapter(context = this@MyTeamApplicationEditActivity)
        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        citySpinner.adapter = cityAdapter
        citySpinner.setSelection(cityAdapter.getPosition(city))
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
                val sigunguAdapter = ArrayAdapter(
                    this@MyTeamApplicationEditActivity,
                    android.R.layout.simple_spinner_item,
                    resources.getStringArray(arrayResource)
                )
                sigunguAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                sigunguSpinner.adapter = sigunguAdapter
                sigunguSpinner.setSelection(sigunguAdapter.getPosition(gu))
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
                    this@MyTeamApplicationEditActivity,
                    android.R.layout.simple_spinner_item,
                    resources.getStringArray(arrayResource)
                )
                dongAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                dongSpinner.adapter = dongAdapter
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
        //기존 데이터 선택값으로 시작
        genderSpinner.setSelection(genderAdapter.getPosition(data!!.gender))


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
        //기존 데이터 선택값으로 시작
        levelSpinner.setSelection(levelAdapter.getPosition(data!!.level))


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
        //기존 데이터 선택값으로 시작
        timeSpinner.setSelection(timeAdapter.getPosition(data!!.schedule))

        //number
        teamNumber.setOnClickListener {
            showNumberPicker()
        }
        teamNumber.text = data!!.playerNum.toString()
        //age
        teamAge.setOnClickListener {
            showAgePicker()
        }
        teamAge.text = data!!.age.toString()

    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showNumberPicker() {
        val bottomSheet = TeamNumberBottomSheet()
        bottomSheet.show(supportFragmentManager, TeamWritingActivity.TEAM_NUMBER_BOTTOM_SHEET)
    }

    private fun showAgePicker() {
        val bottomSheet = TeamAgeBottomSheet()
        bottomSheet.show(supportFragmentManager, TeamWritingActivity.TEAM_AGE_BOTTOM_SHEET)
    }

    private fun uploadToFirebase(
        uri: Uri?,
        data: TeamItem.ApplicationItem,
        newData: TeamItem.ApplicationItem,
    ) {
        val fileRef = reference.child("Team/${data.teamId}")

        if (uri != null) {
            fileRef.putFile(uri)
                .addOnSuccessListener {
                    fileRef.downloadUrl
                        .addOnSuccessListener { uri ->
                            newData.postImg = uri.toString()
                            viewModel.editTeam(data, newData)

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
        } else {
            if (data.postImg == "") {
                binding.progressBar.visibility = View.VISIBLE
                viewModel.editTeam(data, newData)
                binding.progressBar.visibility = View.INVISIBLE
                Toast.makeText(this, "게시글이 수정되었습니다.", Toast.LENGTH_SHORT).show()

            } else {
                binding.progressBar.visibility = View.VISIBLE

                fileRef.delete()
                    .addOnSuccessListener {
                        viewModel.editTeam(data, newData)
                        binding.progressBar.visibility = View.INVISIBLE
                        Toast.makeText(this, "게시글이 수정되었습니다.", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener { exception ->
                        binding.progressBar.visibility = View.INVISIBLE
                        Toast.makeText(this, "게시글 수정을 실패하였습니다. 다시 시도해 주세요.", Toast.LENGTH_SHORT)
                            .show()
                    }
            }
        }
    }
}