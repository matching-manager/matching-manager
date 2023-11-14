package com.link_up.matching_manager.ui.my.match

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.lifecycle.Observer
import coil.load
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.link_up.matching_manager.R
import com.link_up.matching_manager.databinding.MyMatchEditActivityBinding
import com.link_up.matching_manager.ui.match.MatchDataModel
import com.link_up.matching_manager.ui.my.bottomsheet.MyCalenderBottomSheet
import com.link_up.matching_manager.ui.my.bottomsheet.MyNumberBottomSheet
import com.link_up.matching_manager.ui.my.bottomsheet.MyTimeBottomSheet
import com.link_up.matching_manager.ui.my.my.MyEvent
import com.link_up.matching_manager.ui.my.my.MySharedViewModel
import com.link_up.matching_manager.ui.my.my.MyPostViewModel
import com.link_up.matching_manager.ui.my.my.MyPostViewModelFactory
import com.link_up.matching_manager.util.Spinners


class MyMatchEditActivity : AppCompatActivity() {
    private lateinit var binding: MyMatchEditActivityBinding

    private val viewModel: MyPostViewModel by viewModels {
        MyPostViewModelFactory()
    }
    private val sharedViewModel: MySharedViewModel by viewModels()

    private val reference: StorageReference = FirebaseStorage.getInstance().reference

    private var selectedGame: String? = null
    private var selectedGender: String? = null
    private var selectedLevel: String? = null
    private var selectedArea: String? = null


    private val data: MatchDataModel? by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(OBJECT_DATA, MatchDataModel::class.java)
        } else {
            intent.getParcelableExtra<MatchDataModel>(OBJECT_DATA)
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

    private fun setSpinner() = with(binding) {
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
        //기존 데이터 선택값으로 시작
        gameSpinner.setSelection(gameAdapter.getPosition(data!!.game))

        //기존 지역 데이터에서 시, 구 분리
        val areaParts = data!!.matchPlace.split("/")
        val city = areaParts[0]
        val gu = areaParts[1]

        //지역선택 스피너
        val cityAdapter = Spinners.cityAdapter(context = this@MyMatchEditActivity)
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
                    this@MyMatchEditActivity,
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
                    this@MyMatchEditActivity,
                    android.R.layout.simple_spinner_item,
                    resources.getStringArray(arrayResource)
                )
                dongAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                dongSpinner.adapter = dongAdapter
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

        //기존 데이터 선택값으로 시작
        genderSpinner.setSelection(genderAdapter.getPosition(data!!.gender))


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

        //기존 데이터 선택값으로 시작
        levelSpinner.setSelection(levelAdapter.getPosition(data!!.level))

        //일정 데이터 분리
        val schedule = data!!.schedule.split(") ")
        var date = schedule[0] + ")"
        var time = schedule[1]

        //date
        tvMonthDate.setOnClickListener {
            showCalenderPicker()
        }
        tvMonthDate.text = date
        //time
        tvTime.setOnClickListener {
            showTimePicker()
        }
        tvTime.text = time
        //number
        val clickListener = View.OnClickListener {
            showNumberPicker()
        }
        tvTeamNumber1.text = data!!.playerNum.toString()
        tvTeamNumber2.text = data!!.playerNum.toString()
        tvTeamNumber1.setOnClickListener(clickListener)
        tvTeamNumber2.setOnClickListener(clickListener)
    }

    private fun initViewModel() = with(binding) {
        with(viewModel) {
            event.observe(this@MyMatchEditActivity) {
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
        val bottomSheet = MyCalenderBottomSheet()
        bottomSheet.show(supportFragmentManager, MATCH_CALENDER_BOTTOM_SHEET)
    }

    private fun showTimePicker() {
        val bottomSheet = MyTimeBottomSheet()
        bottomSheet.show(supportFragmentManager, MATCH_TIME_BOTTOM_SHEET)
    }

    private fun showNumberPicker() {
        val bottomSheet = MyNumberBottomSheet()
        bottomSheet.show(supportFragmentManager, MATCH_NUMBER_BOTTOM_SHEET)
    }

    private fun initView() = with(binding) {
        //기존 매치 데이터 불러옴(스피너는 setSpinner에)
        etTeamName.setText(data!!.teamName)
        etEntryFee.setText(data!!.entryFee.toString())
        etContent.setText(data!!.description)

        if (data!!.postImg != "") {
            ivTeam.load(data!!.postImg)
            imageUri = data!!.postImg.toUri()
            btnCancelImage.visibility = View.VISIBLE
        }

        btnCancelImage.setOnClickListener {
            imageUri = null
            ivTeam.setImageDrawable(null)
            btnCancelImage.visibility = View.INVISIBLE
        }

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

        btnSubmit.setOnClickListener {
            val teamName =
                etTeamName.text?.toString() ?: "" // Elvis 연산자를 사용하여 null일 경우 ""으로 초기화합니다.
            val game = (gameSpinner.selectedItem?.toString() ?: "")
            val schedule = "${tvMonthDate.text?.toString()} ${tvTime.text?.toString()}"
            val playerNum = tvTeamNumber1.text.toString().toIntOrNull() ?: 0
            val selectedArea =
                citySpinner.selectedItem.toString() + "/" + sigunguSpinner.selectedItem.toString()
            val gender = genderSpinner.selectedItem?.toString() ?: ""
            val level = levelSpinner.selectedItem?.toString() ?: ""
            val entryFee = etEntryFee.text.toString()
            val description = etContent.text?.toString() ?: ""


            val tvMonthDateText = tvMonthDate.text?.toString()
            val tvTimeText = tvTime.text?.toString()
            val teamNumberText = tvTeamNumber1.text?.toString()
            when {
                teamName.isBlank() -> {
                    teamName.let {
                        if (it.isBlank()) {
                            showToast("팀 이름을 입력해 주세요")
                            return@setOnClickListener
                        } else if (it.length >= 10) {
                            showToast("팀 이름은 최대 10자까지 입니다")
                            return@setOnClickListener
                        }
                    }
                }

                game.contains("선택") -> {
                    showToast("경기 종목을 선택해 주세요")
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

                selectedArea.contains("선택") -> {
                    showToast("위치를 선택해 주세요")
                    return@setOnClickListener
                }

                gender.contains("선택") -> {
                    showToast("성별을 선택해 주세요")
                    return@setOnClickListener
                }

                level.contains("선택") -> {
                    showToast("실력을 선택해 주세요")
                    return@setOnClickListener
                }

                entryFee.isBlank() -> {
                    entryFee.let {
                        val fee = it.toIntOrNull()
                        if (it.isBlank()) {
                            showToast("회비를 입력해 주세요")
                            return@setOnClickListener
                        } else if (fee != null && fee % 1000 != 0) {
                            showToast("회비는 천원 단위로 입력해 주세요")
                        }
                    }
                }

                description.isBlank() -> {
                    description.let {
                        if (it.isBlank()) {
                            showToast("내용을 입력해 주세요")
                            return@setOnClickListener
                        } else if (it.length < 10) {
                            showToast("내용은 최소 10글자 이상 입력해 주세요")
                            return@setOnClickListener
                        }
                    }
                }
                else -> {}
            }
            val editData = MatchDataModel(
                teamName = teamName,
                game = game,
                schedule = schedule,
                playerNum = playerNum,
                matchPlace = selectedArea,
                gender = gender,
                level = level,
                entryFee = entryFee.toInt(),
                description = description,
                postImg = ""
            )
            editFromFirebase(imageUri, data!!, editData)
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
            binding.ivTeam.load(imageUri)
            binding.btnCancelImage.visibility = View.VISIBLE
        }
    }

    private fun editFromFirebase(uri: Uri?, data: MatchDataModel, newData: MatchDataModel) {
        val fileRef = reference.child("Match/${data.matchId}")

        if (uri != null) {
            if(imageUri == data.postImg.toUri()) {
                newData.postImg = imageUri.toString()
                binding.progressBar.visibility = View.VISIBLE
                viewModel.editMatch(data, newData)
                binding.progressBar.visibility = View.INVISIBLE
                Toast.makeText(this, "게시글이 수정되었습니다.", Toast.LENGTH_SHORT).show()
            }
            else {
                fileRef.putFile(uri)
                    .addOnSuccessListener {
                        fileRef.downloadUrl
                            .addOnSuccessListener { uri ->
                                newData.postImg = uri.toString()
                                viewModel.editMatch(data, newData)

                                binding.progressBar.visibility = View.INVISIBLE

                                Toast.makeText(this, "게시글이 수정되었습니다.", Toast.LENGTH_SHORT).show()

                            }
                    }
                    .addOnProgressListener { snapshot ->
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    .addOnFailureListener { e ->
                        binding.progressBar.visibility = View.INVISIBLE
                        Toast.makeText(this, "게시글 수정을 실패하였습니다. 다시 시도해 주세요.", Toast.LENGTH_SHORT)
                            .show()
                    }
            }
        } else {
            if (data.postImg == "") {
                binding.progressBar.visibility = View.VISIBLE
                viewModel.editMatch(data, newData)
                binding.progressBar.visibility = View.INVISIBLE
                Toast.makeText(this, "게시글이 수정되었습니다.", Toast.LENGTH_SHORT).show()
            } else {
                binding.progressBar.visibility = View.VISIBLE
                fileRef.delete()
                    .addOnSuccessListener {
                        viewModel.editMatch(data, newData)
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