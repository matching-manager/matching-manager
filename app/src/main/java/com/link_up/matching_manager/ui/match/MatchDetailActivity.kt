package com.link_up.matching_manager.ui.match

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import coil.load
import com.link_up.matching_manager.R
import com.link_up.matching_manager.databinding.MatchDetailActivityBinding
import com.link_up.matching_manager.ui.fcm.send.SendFcmFragment
import com.link_up.matching_manager.ui.fcm.send.SendType
import com.link_up.matching_manager.ui.my.my.MyPostViewModelFactory
import com.link_up.matching_manager.ui.my.my.MyPostViewModel
import com.link_up.matching_manager.ui.my.bookmark.MyBookmarkMatchFragment
import com.google.gson.Gson
import com.link_up.matching_manager.ui.my.my.MyBookmarkViewModel
import com.link_up.matching_manager.ui.my.my.MyBookmarkViewModelFactory
import com.link_up.matching_manager.util.UserInformation
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.concurrent.TimeUnit

class MatchDetailActivity : AppCompatActivity() {

    private lateinit var binding: MatchDetailActivityBinding

    private val viewModel : MyBookmarkViewModel by viewModels {
        MyBookmarkViewModelFactory(application)
    }

    private var isLiked = false

    private val data: MatchDataModel? by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(OBJECT_DATA, MatchDataModel::class.java)
        } else {
            intent.getParcelableExtra<MatchDataModel>(OBJECT_DATA)
        }
    }

    companion object {
        const val OBJECT_DATA = "item_object"
        fun detailIntent(context: Context, item: MatchDataModel): Intent {
            val intent = Intent(context, MatchDetailActivity::class.java)
            intent.putExtra(OBJECT_DATA, item)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MatchDetailActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViewModel()
        initView()
    }

    private fun initView() = with(binding) {
        ivProfile.load(data!!.userImg.toUri())
        when (data?.game) {
            "풋살" -> ivGame.setImageResource(R.drawable.ic_futsal2)
            "축구" -> ivGame.setImageResource(R.drawable.ic_soccerball)
            "농구" -> ivGame.setImageResource(R.drawable.ic_basketball2)
            "배드민턴" -> ivGame.setImageResource(R.drawable.ic_badminton2)
            "볼링" -> ivGame.setImageResource(R.drawable.ic_bowlingball)
        }
        when (data?.gender) {
            "여성" -> ivGender.setImageResource(R.drawable.ic_female)
            "남성" -> ivGender.setImageResource(R.drawable.ic_male)
            "혼성" -> ivGender.setImageResource(R.drawable.ic_mix)
        }
        tvLevel.text = data!!.level
        when (data?.level) {
            "하(Lv1-3)" -> ivLevel.setImageResource(R.drawable.ic_level3)
            "중(Lv4-6)" -> ivLevel.setImageResource(R.drawable.ic_level2)
            "상(Lv7-10)" -> ivLevel.setImageResource(R.drawable.ic_level1)
        }
        tvArenaTitle.text = "[${data!!.game}] [${data!!.schedule}]"
        tvArenaTitle2.text = data!!.matchPlace
        tvNickname.text = data!!.userNickname
        tvTime.text = calculationTime(dateTimeToMillSec(data!!.uploadTime))
        tvViewCount.text = data!!.viewCount.toString()
        tvChatCount.text = data!!.chatCount.toString()
        tvType.text = data!!.game
        tvPlayerNum.text = "${data!!.playerNum} VS ${data!!.playerNum}"
        tvPay.text = decimalFormat(data!!.entryFee)
        tvTeamName.text = data!!.teamName
        tvDescription.text = data!!.description
        if (data!!.postImg != "") ivTeam.load(data!!.postImg.toUri())
        else cvPhoto1.visibility = View.INVISIBLE

        if(data!!.userId == UserInformation.userInfo.uid) {
            bottomSheetLayout.visibility = View.GONE
        }

        btnCancel.setOnClickListener {
            finish()
        }

        btnCallMatch.setOnClickListener {
            SendFcmFragment().apply {
                arguments =
                    Bundle().apply {
                        putString(SendFcmFragment.INPUT_TYPE, SendType.MATCH.name)
                        putString(SendFcmFragment.FCM_TOKEN, data!!.fcmToken)
                    }
            }.show(supportFragmentManager, "SampleDialog")
        }
        ivBookmark.setOnClickListener {
            if (!isLiked) {
                viewModel.insertBookmarkMatchData(data!!)
                ivBookmark.setImageResource(R.drawable.ic_heart_filled)
                isLiked = true
            } else {
                viewModel.deleteBookmarkMatchData(data!!)
                ivBookmark.setImageResource(R.drawable.ic_heart)
                isLiked = false
            }

        }
    }

    private fun initViewModel() = with(viewModel) {
        bookmarkMatchList.observe(this@MatchDetailActivity, Observer { it ->
            if( it != null) {
                for(bookmark in it) {
                    if(bookmark.matchId == data!!.matchId) {
                        isLiked = true
                        binding.ivBookmark.setImageResource(R.drawable.ic_heart_filled)
                        break
                    }
                }
            }
        })
    }

    private fun decimalFormat(entryFee: Int): String {
        val dec = DecimalFormat("#,###")

        return "${dec.format(entryFee)}원"
    }

    @SuppressLint("SimpleDateFormat")
    private fun dateTimeToMillSec(dateTime: String): Long {
        var timeInMilliseconds: Long = 0
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

        val mDate = sdf.parse(dateTime)
        if (mDate != null) {
            timeInMilliseconds = mDate.time
        }

        return timeInMilliseconds
    }

    private fun calculationTime(createDateTime: Long): String {
        val nowDateTime = Calendar.getInstance().timeInMillis //현재 시간 to millisecond
        var value = ""
        val differenceValue = nowDateTime - createDateTime //현재 시간 - 비교가 될 시간
        when {
            differenceValue < 60000 -> { //59초 보다 적다면
                value = "방금 전"
            }

            differenceValue < 3600000 -> { //59분 보다 적다면
                value = TimeUnit.MILLISECONDS.toMinutes(differenceValue).toString() + "분 전"
            }

            differenceValue < 86400000 -> { //23시간 보다 적다면
                value = TimeUnit.MILLISECONDS.toHours(differenceValue).toString() + "시간 전"
            }

            differenceValue < 604800000 -> { //7일 보다 적다면
                value = TimeUnit.MILLISECONDS.toDays(differenceValue).toString() + "일 전"
            }

            differenceValue < 2419200000 -> { //3주 보다 적다면
                value = (TimeUnit.MILLISECONDS.toDays(differenceValue) / 7).toString() + "주 전"
            }

            differenceValue < 31556952000 -> { //12개월 보다 적다면
                value = (TimeUnit.MILLISECONDS.toDays(differenceValue) / 30).toString() + "개월 전"
            }

            else -> { //그 외
                value = (TimeUnit.MILLISECONDS.toDays(differenceValue) / 365).toString() + "년 전"
            }
        }
        return value
    }
}