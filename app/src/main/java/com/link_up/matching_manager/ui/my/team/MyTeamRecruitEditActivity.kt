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
import com.link_up.matching_manager.databinding.MyTeamRecruitEditActivityBinding
import com.link_up.matching_manager.ui.my.my.MyEvent
import com.link_up.matching_manager.ui.my.my.MyViewModel
import com.link_up.matching_manager.ui.my.match.MyMatchMenuBottomSheet
import com.link_up.matching_manager.ui.my.match.MyMatchViewModelFactory
import com.link_up.matching_manager.ui.team.TeamItem
import com.link_up.matching_manager.ui.team.TeamWritingActivity
import com.link_up.matching_manager.ui.team.bottomsheet.TeamCalenderBottomSheet
import com.link_up.matching_manager.ui.team.bottomsheet.TeamNumberBottomSheet
import com.link_up.matching_manager.ui.team.bottomsheet.TeamTimeBottomSheet
import com.link_up.matching_manager.ui.team.view_model.TeamSharedViewModel
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.link_up.matching_manager.util.Spinners

class MyTeamRecruitEditActivity : AppCompatActivity() {
    private lateinit var binding: MyTeamRecruitEditActivityBinding

    private val sharedViewModel: TeamSharedViewModel by viewModels()

    private val viewModel: MyViewModel by viewModels {
        MyMatchViewModelFactory()
    }

    private val reference: StorageReference = FirebaseStorage.getInstance().reference

    private var selectedGame: String? = null
    private var selectedArea: String? = null
    private var selectedGender: String? = null
    private var selectedLevel: String? = null
    private var imageUri: Uri? = null

    private val data: TeamItem.RecruitmentItem? by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(
                OBJECT_DATA,
                TeamItem.RecruitmentItem::class.java
            )
        } else {
            intent.getParcelableExtra<TeamItem.RecruitmentItem>(OBJECT_DATA)
        }
    }

    companion object {
        const val OBJECT_DATA = "item_object"

        fun editIntent(
            context: Context, item: TeamItem.RecruitmentItem
        ): Intent {
            val intent = Intent(context, MyTeamRecruitEditActivity::class.java)
            intent.putExtra(OBJECT_DATA, item)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MyTeamRecruitEditActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSpinner()
        initView()
        initViewModel()
    }

    private fun initView() = with(binding) {
        tvTeamName.setText(data!!.teamName)
        tvFee.setText(data!!.pay.toString())
        etContent.setText(data!!.description)

        if(data!!.postImg != "") {
            ivImage.load(data!!.postImg)
            btnCancelImage.visibility = View.VISIBLE
        }

        btnCancelImage.setOnClickListener {
            imageUri = null
            ivImage.setImageDrawable(null)
            btnCancelImage.visibility = View.INVISIBLE
        }

        val intent = Intent(this@MyTeamRecruitEditActivity, MyMatchMenuBottomSheet::class.java)
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
            val selectedFee = tvFee.text.toString()
            val selectedTeamName = tvTeamName.text.toString()
            val setContent = etContent.text.toString()
            val selectedNumber = sharedViewModel.number.value ?: 0 // 기본값을 0으로 설정
            val selectedDate = tvMonthDate.text.toString()
            val selectedTime = tvTime.text.toString()

            val tvMonthDateText = tvMonthDate.text?.toString()
            val tvTimeText = tvTime.text?.toString()
            val teamNumberText = teamNumber.text?.toString()

            when {
                selectedGame.contains("선택") -> {
                    showToast("경기 종목을 선택해 주세요")
                    layoutGame.isSelected = true
                    layoutGame.isFocusable = false
                    return@setOnClickListener
                }

                selectedArea.contains("선택") -> {
                    showToast("지역을 선택해 주세요")
                    return@setOnClickListener
                }

                tvMonthDateText.isNullOrEmpty() -> {
                    showToast("일정을 선택해 주세요")
                    return@setOnClickListener
                }

                tvTimeText.isNullOrEmpty() -> {
                    showToast("시간 선택해 주세요")
                    return@setOnClickListener
                }

                teamNumberText.isNullOrEmpty() -> {
                    showToast("인원을 선택해 주세요")
                    return@setOnClickListener
                }

                selectedTeamName.isBlank() -> {
                    selectedTeamName.let {
                        if (it.isBlank()) {
                            showToast("팀 이름을 입력해 주세요")
                            return@setOnClickListener
                        } else if (it.length >= 10) {
                            showToast("팀 이름은 최대 10자까지 입니다")
                            return@setOnClickListener
                        }
                    }
                }

                selectedFee.isBlank() -> {
                    selectedFee.let {
                        val fee = it.toIntOrNull()
                        if (it.isBlank()) {
                            showToast("회비를 입력해 주세요")
                            return@setOnClickListener
                        } else if (fee != null && fee % 1000 != 0) {
                            showToast("회비는 천원 단위로 입력해 주세요")
                        }
                    }
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

            val editTeam = TeamItem.RecruitmentItem(
                description = setContent,
                gender = selectedGender,
                level = selectedLevel,
                playerNum = selectedNumber,
                schedule = "$selectedDate $selectedTime",//경기일정으로 되어있음 -> 달력바텀시트 만들어야함
                game = selectedGame,
                area = selectedArea,//지역 설정하기 스피너 추가해야함
                pay = selectedFee.toInt(),
                teamName = selectedTeamName
            )

            uploadToFirebase(imageUri, data!!, editTeam)
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
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

    private fun initViewModel() = with(binding) {
        with(viewModel) {
            event.observe(this@MyTeamRecruitEditActivity) {
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
            number.observe(this@MyTeamRecruitEditActivity, Observer {
                Log.d("teamNumber", "activity = $it")
                teamNumber.text = it.toString()
            })
            teamTime.observe(this@MyTeamRecruitEditActivity, Observer { (hour, minute, amPm) ->
                val time = String.format("%s %02d:%02d", amPm, hour, minute)
                Log.d("teamTime", "activity = $time")
                tvTime.text = time
            })
            calendar.observe(
                this@MyTeamRecruitEditActivity,
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
            this@MyTeamRecruitEditActivity,
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
        val cityAdapter = Spinners.cityAdapter(context = this@MyTeamRecruitEditActivity)
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
                    this@MyTeamRecruitEditActivity,
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
                    this@MyTeamRecruitEditActivity,
                    android.R.layout.simple_spinner_item,
                    resources.getStringArray(arrayResource)
                )
                dongAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                dongSpinner.adapter = dongAdapter
            }
        }

        //성별 스피너
        val genderAdapter = ArrayAdapter.createFromResource(
            this@MyTeamRecruitEditActivity,
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
            this@MyTeamRecruitEditActivity,
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

    }

    private fun showCalenderPicker() {
        val bottomSheet = TeamCalenderBottomSheet()
        bottomSheet.show(supportFragmentManager, TeamWritingActivity.TEAM_CALENDER_BOTTOM_SHEET)
    }

    private fun showTimePicker() {
        val bottomSheet = TeamTimeBottomSheet()
        bottomSheet.show(supportFragmentManager, TeamWritingActivity.TEAM_TIME_BOTTOM_SHEET)
    }

    private fun showNumberPicker() {
        val bottomSheet = TeamNumberBottomSheet()
        bottomSheet.show(supportFragmentManager, TeamWritingActivity.TEAM_NUMBER_BOTTOM_SHEET)
    }

    private fun uploadToFirebase(uri: Uri?, data: TeamItem.RecruitmentItem, newData: TeamItem.RecruitmentItem) {
        val fileRef = reference.child("Team/${data.teamId}")

        if (uri != null) {
            fileRef.putFile(uri)
                .addOnSuccessListener {
                    fileRef.downloadUrl
                        .addOnSuccessListener { uri ->
                            newData.postImg = uri.toString()
                            viewModel.editRecruit(data, newData)

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
        else {
            if (data.postImg == "") {
                binding.progressBar.visibility = View.VISIBLE
                viewModel.editRecruit(data, newData)
                binding.progressBar.visibility = View.INVISIBLE
                Toast.makeText(this, "게시글이 수정되었습니다.", Toast.LENGTH_SHORT).show()
            }
            else {
                binding.progressBar.visibility = View.VISIBLE

                fileRef.delete()
                    .addOnSuccessListener {
                        viewModel.editRecruit(data, newData)
                        binding.progressBar.visibility = View.INVISIBLE
                        Toast.makeText(this, "게시글이 수정되었습니다.", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener { exception ->
                        binding.progressBar.visibility = View.INVISIBLE
                        Toast.makeText(this, "게시글 수정을 실패하였습니다. 다시 시도해 주세요.", Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }
}