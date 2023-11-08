package com.example.matching_manager.ui.my.team

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.net.toUri
import coil.load
import com.example.matching_manager.databinding.MyTeamRecruitDetailActivityBinding
import com.example.matching_manager.ui.team.TeamItem
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.concurrent.TimeUnit

class MyTeamRecruitDetailActivity : AppCompatActivity() {
    private lateinit var binding : MyTeamRecruitDetailActivityBinding

    private val data: TeamItem.RecruitmentItem? by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(OBJECT_DATA, TeamItem.RecruitmentItem::class.java)
        } else {
            intent.getParcelableExtra<TeamItem.RecruitmentItem>(OBJECT_DATA)
        }
    }

    companion object {
        const val OBJECT_DATA = "item_object"
        fun detailIntent(
            context: Context, item: TeamItem.RecruitmentItem
        ): Intent {
            val intent = Intent(context, MyTeamRecruitDetailActivity::class.java)
            intent.putExtra(OBJECT_DATA, item)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MyTeamRecruitDetailActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
    }

    private fun initView() = with(binding) {
        tvTitle.text = "[${data!!.game}] ${data!!.schedule}"
        tvTitle2.text = data!!.area
        ivProfile.load(data!!.userImg)
        tvPlayerNum.text = "${data!!.playerNum}명"
        tvPay.text = decimalFormat(data!!.pay)
        tvTeamName.text = data!!.teamName
        tvGender.text = data!!.gender
        tvChatCount.text = data!!.chatCount.toString()
        tvViewCount.text = data!!.viewCount.toString()
        tvNicname.text = data!!.nickname
        tvContent.text = data!!.description
        tvTime.text = calculationTime(dateTimeToMillSec(data!!.uploadTime))
        tvLevel.text = data!!.level
        if(data!!.postImg != "") ivImage.load(data!!.postImg.toUri())
        else cvPhoto1.visibility = View.INVISIBLE

        btnCancel.setOnClickListener {
            finish()
        }
    }

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