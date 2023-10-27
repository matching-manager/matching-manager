package com.example.matching_manager.ui.match

import android.annotation.SuppressLint
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.net.toUri
import coil.load
import com.example.matching_manager.R
import com.example.matching_manager.databinding.MatchDetailActivityBinding
import com.example.matching_manager.ui.fcm.send.SendFcmFragment
import com.example.matching_manager.ui.fcm.send.SendType
import com.example.matching_manager.ui.match.MatchFragment.Companion.OBJECT_DATA
import com.google.android.material.bottomsheet.BottomSheetBehavior
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.concurrent.TimeUnit

class MatchDetailActivity : AppCompatActivity() {

    private lateinit var binding: MatchDetailActivityBinding

//    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
//
//    private val bottomSheetLayout by lazy { findViewById<ConstraintLayout>(R.id.bottom_sheet_layout) }
//    private val bottomSheetBookmarkButton by lazy { findViewById<ImageView>(R.id.iv_bookmark) }
//    private val bottomSheetCallMatchButton by lazy { findViewById<Button>(R.id.btn_call_match) }

    private val data: MatchDataModel? by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(OBJECT_DATA, MatchDataModel::class.java)
        } else {
            intent.getParcelableExtra<MatchDataModel>(OBJECT_DATA)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MatchDetailActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
    }

    private fun initView() = with(binding) {
        data?.let { ivProfile.setImageResource(it.userImg) }
        tvNickname.text = data!!.userId
        tvTime.text = calculationTime(dateTimeToMillSec(data!!.uploadTime))
        tvChatCount.text = data!!.chatCount.toString()
        tvType.text = data!!.game
        tvPlayerNum.text = "${data!!.playerNum} VS ${data!!.playerNum}"
        tvGender.text = data!!.gender
        tvPay.text = decimalFormat(data!!.entryFee)
        tvTeamName.text = data!!.teamName
        ivTeam.load(data!!.postImg.toUri())

        btnCancel.setOnClickListener {
            finish()
        }

        btnCallMatch.setOnClickListener {
            SendFcmFragment().apply {
                arguments = Bundle().apply { putString(SendFcmFragment.INPUT_TYPE, SendType.MATCH.name) }
            }.show(supportFragmentManager, "SampleDialog")
        }


//        initializePersistentBottomSheet()

//        scrollView.setOnScrollChangeListener { _, _, scrollY, _, oldScrollY ->
//            if (scrollY > 0) {
//                // 아래로 스크롤 중
//                bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
//            } else {
//                // 위로 스크롤 중
//                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
//            }
//            if (oldScrollY > scrollY) {
//                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
//            }
//        }

    }

//    private fun initializePersistentBottomSheet() {
//
//        // BottomSheetBehavior에 layout 설정
//        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetLayout)
//
//        bottomSheetBehavior.addBottomSheetCallback(object :
//            BottomSheetBehavior.BottomSheetCallback() {
//            override fun onStateChanged(bottomSheet: View, newState: Int) {
//
//                // BottomSheetBehavior state에 따른 이벤트
//                when (newState) {
//                    BottomSheetBehavior.STATE_HIDDEN -> {
//                        Log.d("MatchDetailActivity", "state: hidden")
//                    }
//
//                    BottomSheetBehavior.STATE_EXPANDED -> {
//                        Log.d("MatchDetailActivity", "state: expanded")
//                    }
//
//                    BottomSheetBehavior.STATE_COLLAPSED -> {
//                        Log.d("MatchDetailActivity", "state: collapsed")
//                    }
//
//                    BottomSheetBehavior.STATE_DRAGGING -> {
//                        Log.d("MatchDetailActivity", "state: dragging")
//                    }
//
//                    BottomSheetBehavior.STATE_SETTLING -> {
//                        Log.d("MatchDetailActivity", "state: settling")
//                    }
//
//                    BottomSheetBehavior.STATE_HALF_EXPANDED -> {
//                        Log.d("MatchDetailActivity", "state: half expanded")
//                    }
//                }
//
//            }
//
//            override fun onSlide(bottomSheet: View, slideOffset: Float) {
//            }
//
//        })
//    }

    private fun decimalFormat(entryFee : Int) : String {
        val dec = DecimalFormat("#,###")

        return "${dec.format(entryFee)}원"
    }
    @SuppressLint("SimpleDateFormat")
    private fun dateTimeToMillSec(dateTime: String): Long{
        var timeInMilliseconds: Long = 0
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

        val mDate = sdf.parse(dateTime)
        if (mDate != null) {
            timeInMilliseconds = mDate.time
        }

        return timeInMilliseconds
    }

    private fun calculationTime(createDateTime: Long): String{
        val nowDateTime = Calendar.getInstance().timeInMillis //현재 시간 to millisecond
        var value = ""
        val differenceValue = nowDateTime - createDateTime //현재 시간 - 비교가 될 시간
        when {
            differenceValue < 60000 -> { //59초 보다 적다면
                value = "방금 전"
            }
            differenceValue < 3600000 -> { //59분 보다 적다면
                value =  TimeUnit.MILLISECONDS.toMinutes(differenceValue).toString() + "분 전"
            }
            differenceValue < 86400000 -> { //23시간 보다 적다면
                value =  TimeUnit.MILLISECONDS.toHours(differenceValue).toString() + "시간 전"
            }
            differenceValue <  604800000 -> { //7일 보다 적다면
                value =  TimeUnit.MILLISECONDS.toDays(differenceValue).toString() + "일 전"
            }
            differenceValue < 2419200000 -> { //3주 보다 적다면
                value =  (TimeUnit.MILLISECONDS.toDays(differenceValue)/7).toString() + "주 전"
            }
            differenceValue < 31556952000 -> { //12개월 보다 적다면
                value =  (TimeUnit.MILLISECONDS.toDays(differenceValue)/30).toString() + "개월 전"
            }
            else -> { //그 외
                value =  (TimeUnit.MILLISECONDS.toDays(differenceValue)/365).toString() + "년 전"
            }
        }
        return value
    }
}