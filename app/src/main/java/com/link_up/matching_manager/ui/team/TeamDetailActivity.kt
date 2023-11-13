package com.link_up.matching_manager.ui.team

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import coil.load
import com.google.gson.Gson
import com.link_up.matching_manager.R
import com.link_up.matching_manager.databinding.TeamDetailActivityBinding
import com.link_up.matching_manager.ui.fcm.send.SendFcmFragment
import com.link_up.matching_manager.ui.fcm.send.SendType
import com.link_up.matching_manager.ui.my.bookmark.BookmarkApplicationDataModel
import com.link_up.matching_manager.ui.my.bookmark.BookmarkRecruitDataModel
import com.link_up.matching_manager.ui.my.bookmark.MyBookmarkApplicationFragment
import com.link_up.matching_manager.ui.my.bookmark.MyBookmarkRecruitFragment
import com.link_up.matching_manager.ui.my.match.MyMatchViewModelFactory
import com.link_up.matching_manager.ui.my.my.MyViewModel
import com.link_up.matching_manager.util.UserInformation
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.concurrent.TimeUnit


class TeamDetailActivity : AppCompatActivity() {
    private lateinit var binding: TeamDetailActivityBinding

    private val viewModel: MyViewModel by viewModels {
        MyMatchViewModelFactory()
    }

    private var isLiked = false

    companion object {
        private const val TAG = "TeamDetailActivity"
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

        if(item!!.userId == UserInformation.userInfo.uid) {
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
                getTeamBookmark()
                ivBookmark.setImageResource(R.drawable.ic_heart_filled)
                isLiked = true
            } else {
                deleteTeamBookmark()
                getTeamBookmark()
                ivBookmark.setImageResource(R.drawable.ic_heart)
                isLiked = false
            }
        }
        val bookmarkPref = getSharedPreferences("Bookmark", Context.MODE_PRIVATE)
        val keys = bookmarkPref.all.keys

        for (key in keys) {
            if (key == "Recruit_${item!!.teamId}" || key == "Application_${item!!.teamId}") {
                ivBookmark.setImageResource(R.drawable.ic_heart_filled)
                isLiked = true
            }
        }

        val recruitIntent = Intent(this@TeamDetailActivity, MyBookmarkRecruitFragment::class.java)
        setResult(RESULT_OK, recruitIntent)
        val applicationIntent =
            Intent(this@TeamDetailActivity, MyBookmarkApplicationFragment::class.java)
        setResult(RESULT_OK, applicationIntent)
    }

    private fun addTeamBookmark() {
        val item: TeamItem? = intent.getParcelableExtra(OBJECT_DATA)

        val bookmarkPref = getSharedPreferences("Bookmark", Context.MODE_PRIVATE)
        val editor = bookmarkPref.edit()

        val gson = Gson()

        if (item is TeamItem.RecruitmentItem) {
            val recruitItem = teamItemToBookmarkRecruit(item)
            val teamDataJson = gson.toJson(recruitItem)

            editor.putString("Recruit_${item.teamId}", teamDataJson)
            editor.apply()
        } else if (item is TeamItem.ApplicationItem) {
            val applicationItem = teamItemToBookmarkApplication(item)
            val teamDataJson = gson.toJson(applicationItem)

            editor.putString("Application_${item.teamId}", teamDataJson)
            editor.apply()
        }
    }

    private fun deleteTeamBookmark() {
        val item: TeamItem? = intent.getParcelableExtra(OBJECT_DATA)
        val bookmarkPref = getSharedPreferences("Bookmark", Context.MODE_PRIVATE)
        val editor = bookmarkPref.edit()

        if (item!!.type == "용병모집") {
            editor.remove("Recruit_${item.teamId}")
            editor.apply()
        } else {
            editor.remove("Application_${item.teamId}")
            editor.apply()
        }
    }

    private fun getTeamBookmark() {
        val item: TeamItem? = intent.getParcelableExtra(OBJECT_DATA)
        val bookmarkPref = getSharedPreferences("Bookmark", Context.MODE_PRIVATE)
        val gson = Gson()
        val keys = bookmarkPref.all.keys

        if (item!!.type == "용병모집") {
            val dataList = mutableListOf<TeamItem.RecruitmentItem>()
            for (key in keys) {
                if (key.startsWith("Recruit_")) {
                    val json = bookmarkPref.getString(key, null)
                    if (!json.isNullOrBlank()) {
                        val data = gson.fromJson(json, BookmarkRecruitDataModel::class.java)
                        dataList.add(bookmarkRecruitToTeamItem(data))
                    }
                }
            }
            viewModel.addBookmarkRecruitLiveData(dataList)
        } else {
            val dataList = mutableListOf<TeamItem.ApplicationItem>()
            for (key in keys) {
                if (key.startsWith("Application_")) {
                    val json = bookmarkPref.getString(key, null)
                    if (!json.isNullOrBlank()) {
                        val data = gson.fromJson(json, BookmarkApplicationDataModel::class.java)
                        dataList.add(bookmarkApplicationToTeamItem(data))
                    }
                }
            }
            viewModel.addBookmarkApplicationLiveData(dataList)
        }

    }

    private fun teamItemToBookmarkRecruit(item: TeamItem.RecruitmentItem): BookmarkRecruitDataModel {
        return BookmarkRecruitDataModel(
            type = item.type,
            teamId = item.teamId,
            userId = item.userId,
            nickname = item.nickname,
            userImg = item.userImg,
            userEmail = item.userEmail,
            phoneNum = item.phoneNum,
            fcmToken = item.fcmToken,
            description = item.description,
            gender = item.gender,
            chatCount = item.chatCount,
            level = item.level,
            playerNum = item.playerNum,
            postImg = item.postImg,
            schedule = item.schedule,
            uploadTime = item.uploadTime,
            viewCount = item.viewCount,
            game = item.game,
            area = item.area,
            pay = item.pay,
            teamName = item.teamName
        )
    }

    private fun teamItemToBookmarkApplication(item: TeamItem.ApplicationItem): BookmarkApplicationDataModel {
        return BookmarkApplicationDataModel(
            type = item.type,
            teamId = item.teamId,
            userId = item.userId,
            nickname = item.nickname,
            userImg = item.userImg,
            userEmail = item.userEmail,
            phoneNum = item.phoneNum,
            fcmToken = item.fcmToken,
            description = item.description,
            gender = item.gender,
            chatCount = item.chatCount,
            level = item.level,
            playerNum = item.playerNum,
            postImg = item.postImg,
            schedule = item.schedule,
            uploadTime = item.uploadTime,
            viewCount = item.viewCount,
            game = item.game,
            area = item.area,
            age = item.age
        )
    }

    private fun bookmarkRecruitToTeamItem(item : BookmarkRecruitDataModel) : TeamItem.RecruitmentItem {
        return TeamItem.RecruitmentItem(
            type = item.type,
            teamId = item.teamId,
            userId = item.userId,
            nickname = item.nickname,
            userImg = item.userImg,
            userEmail = item.userEmail,
            phoneNum = item.phoneNum,
            fcmToken = item.fcmToken,
            description = item.description,
            gender = item.gender,
            chatCount = item.chatCount,
            level = item.level,
            playerNum = item.playerNum,
            postImg = item.postImg,
            schedule = item.schedule,
            uploadTime = item.uploadTime,
            viewCount = item.viewCount,
            game = item.game,
            area = item.area,
            pay = item.pay,
            teamName = item.teamName
        )
    }

    private fun bookmarkApplicationToTeamItem(item : BookmarkApplicationDataModel) : TeamItem.ApplicationItem {
        return TeamItem.ApplicationItem(
            type = item.type,
            teamId = item.teamId,
            userId = item.userId,
            nickname = item.nickname,
            userImg = item.userImg,
            userEmail = item.userEmail,
            phoneNum = item.phoneNum,
            fcmToken = item.fcmToken,
            description = item.description,
            gender = item.gender,
            chatCount = item.chatCount,
            level = item.level,
            playerNum = item.playerNum,
            postImg = item.postImg,
            schedule = item.schedule,
            uploadTime = item.uploadTime,
            viewCount = item.viewCount,
            game = item.game,
            area = item.area,
            age = item.age
        )
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