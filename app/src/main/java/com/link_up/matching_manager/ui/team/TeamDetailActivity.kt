package com.link_up.matching_manager.ui.team

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.lifecycle.Observer
import coil.load
import com.link_up.matching_manager.R
import com.link_up.matching_manager.databinding.TeamDetailActivityBinding
import com.link_up.matching_manager.ui.fcm.send.SendFcmFragment
import com.link_up.matching_manager.ui.fcm.send.SendType
import com.link_up.matching_manager.ui.my.my.MyBookmarkViewModel
import com.link_up.matching_manager.ui.my.my.MyBookmarkViewModelFactory
import com.link_up.matching_manager.util.UserInformation
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.concurrent.TimeUnit


class TeamDetailActivity : AppCompatActivity() {
    private lateinit var binding: TeamDetailActivityBinding

    private val viewModel: MyBookmarkViewModel by viewModels {
        MyBookmarkViewModelFactory(application)
    }

    private val data: TeamItem? by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(OBJECT_DATA, TeamItem::class.java)
        } else {
            intent.getParcelableExtra<TeamItem>(OBJECT_DATA)
        }
    }

    private var isLiked = false

    companion object {
        private const val OBJECT_DATA = "item_object"
        fun newIntent(
            item: TeamItem,
            context: Context,
        ): Intent {
            val intent = Intent(context, TeamDetailActivity::class.java)
            intent.putExtra(OBJECT_DATA, item)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = TeamDetailActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViewModel()
        initView()
    }

    private fun initView() = with(binding) {
        //진입타입 설정하기
        val item: TeamItem? = intent.getParcelableExtra(OBJECT_DATA)

        if (item is TeamItem.RecruitmentItem) {
            // 용병모집 아이템인 경우
            ivType.setImageResource(R.drawable.ic_recruitment)
            when (item.gender) {
                "여성" -> ivGender.setImageResource(R.drawable.ic_female)
                "남성" -> ivGender.setImageResource(R.drawable.ic_male)
                "혼성" -> ivGender.setImageResource(R.drawable.ic_mix)
            }
            when (item.level) {
                "하(Lv1-3)" -> ivLevel.setImageResource(R.drawable.ic_level3)
                "중(Lv4-6)" -> ivLevel.setImageResource(R.drawable.ic_level2)
                "상(Lv7-10)" -> ivLevel.setImageResource(R.drawable.ic_level1)
            }
            tvType.text = item.type
            btnCancel.title = item.type
            tvTitle.text = "[${item.game}] ${item.schedule}"
            tvTitle2.text = item.area
            ivProfile.load(item.userImg)
            tvPlayerNum.text = "${item.playerNum}명"
            tvFee.text = "회비"
            tvPay.text = decimalFormat(item.pay)
            tvTeam.text = "팀이름"
            tvTeamName.text = item.teamName
            tvChatCount.text = item.chatCount.toString()
            tvViewCount.text = item.viewCount.toString()
            tvNicname.text = item.nickname
            tvContent.text = item.description
            tvTime.text = calculationTime(dateTimeToMillSec(item.uploadTime))
            tvLevel.text = item.level
            btnSubmit.setText(R.string.team_detail_recruitment)
            if (item.postImg != "") ivImage.load(item.postImg.toUri())
            else cvPhoto1.visibility = View.INVISIBLE
            //경기장위치 추가해야함


        } else if (item is TeamItem.ApplicationItem) {
            // 용병신청 아이템인 경우
            ivType.setImageResource(R.drawable.ic_application)
            when (item.gender) {
                "여성" -> ivGender.setImageResource(R.drawable.ic_female)
                "남성" -> ivGender.setImageResource(R.drawable.ic_male)
                "혼성" -> ivGender.setImageResource(R.drawable.ic_mix)
            }
            when (item.level) {
                "하(Lv1-3)" -> ivLevel.setImageResource(R.drawable.ic_level3)
                "중(Lv4-6)" -> ivLevel.setImageResource(R.drawable.ic_level2)
                "상(Lv7-10)" -> ivLevel.setImageResource(R.drawable.ic_level1)
            }
            tvType.text = item.type
            btnCancel.title = item.type
            tvTitle.text = "[${item.game}] ${item.schedule}"
            tvTitle2.text = item.area
            ivProfile.load(item.userImg)
            tvPlayerNum.text = "${item.playerNum}명"
            tvFee.text = "나이"
            tvPay.text = "${item.age}살"//나이가 들어가야함
            tvTeam.text = "가능 시간"
            tvTeamName.text = item.schedule//가능시간이 들어가야함
            tvChatCount.text = item.chatCount.toString()
            tvViewCount.text = item.viewCount.toString()
            tvNicname.text = item.nickname
            tvContent.text = item.description
            tvTime.text = calculationTime(dateTimeToMillSec(item.uploadTime))
            tvLevel.text = item.level
            if (item.postImg != "") ivImage.load(item.postImg.toUri())
            else cvPhoto1.visibility = View.INVISIBLE
            btnSubmit.setText(R.string.team_detail_application)


        }

        if (item!!.userId == UserInformation.userInfo.uid) {
            bottomSheetLayout.visibility = View.GONE
        }

        //back button
        btnCancel.setOnClickListener {
            finish() // 현재 Activity 종료
        }

        //submit button
        btnSubmit.setOnClickListener {
            val item: TeamItem? = intent.getParcelableExtra(OBJECT_DATA)
            SendFcmFragment().apply {
                arguments = Bundle().apply {
                    putString(SendFcmFragment.INPUT_TYPE, SendType.MERCENARY.name)
                    putString(SendFcmFragment.FCM_TOKEN, item!!.fcmToken)
                }
            }.show(supportFragmentManager, "SampleDialog")
        }

        ivBookmark.setOnClickListener {
            if (!isLiked) {
                addTeamBookmark()
                ivBookmark.setImageResource(R.drawable.ic_heart_filled)
                isLiked = true
            } else {
                deleteTeamBookmark()
                ivBookmark.setImageResource(R.drawable.ic_heart)
                isLiked = false
            }
        }
    }

    private fun initViewModel() = with(viewModel) {
        bookmarkApplicationList.observe(this@TeamDetailActivity, Observer { it ->
            for (application in it) {
                if (application.teamId == data!!.teamId) {
                    isLiked = true
                    binding.ivBookmark.setImageResource(R.drawable.ic_heart_filled)
                    break
                }
            }
        })
        bookmarkRecruitList.observe(this@TeamDetailActivity, Observer {
            for (recruitment in it) {
                if (recruitment.teamId == data!!.teamId) {
                    isLiked = true
                    binding.ivBookmark.setImageResource(R.drawable.ic_heart_filled)
                    break
                }
            }
        })
    }

    private fun addTeamBookmark() {
        val item: TeamItem? = intent.getParcelableExtra(OBJECT_DATA)

        if (item is TeamItem.RecruitmentItem) {
            viewModel.insertBookmarkRecruitmentData(item)
        } else if (item is TeamItem.ApplicationItem) {
            viewModel.insertBookmarkApplicationData(item)
        }
    }

    private fun deleteTeamBookmark() {
        val item: TeamItem? = intent.getParcelableExtra(OBJECT_DATA)

        if (item is TeamItem.RecruitmentItem) {
            viewModel.deleteBookmarkRecruitmentData(item)
        } else if (item is TeamItem.ApplicationItem) {
            viewModel.deleteBookmarkApplicationData(item)
        }
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