package com.example.matching_manager.ui.team

import android.content.Context
import android.content.Intent
import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import coil.load
import com.example.matching_manager.R
import com.example.matching_manager.databinding.TeamDetailActivityBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging

class TeamDetailActivity : AppCompatActivity() {
    private lateinit var binding: TeamDetailActivityBinding

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
            tvType.text = item.type
            tvDialogInfo.text = item.type
            tvGame.text = item.game
            tvArea.text = item.area
            tvSchedule.text = item.schedule
            ivProfile.load(item.teamProfile)
            tvPlayerNum.text = item.playerNum
            tvFee.text = "회의비"
            tvPay.text = item.pay
            tvTeam.text = "팀이름"
            tvTeamName.text = item.teamName
            tvGender.text = item.gender
            tvViewCount.text = item.viewCount.toString()
            tvNicname.text = item.nicname
            tvContent.text = item.content
            tvTime.text = item.creationTime
            tvLevel.text = item.level
            btnSubmit.setText(R.string.team_detail_recruitment)
            //경기장위치 추가해야함


        } else if (item is TeamItem.ApplicationItem) {
            // 용병신청 아이템인 경우
            tvType.text = item.type
            tvDialogInfo.text = item.type
            tvGame.text = item.game
            tvArea.text = item.area
            tvSchedule.text = item.schedule//제목이 들어가야함
            ivProfile.load(item.teamProfile)
            tvPlayerNum.text = item.playerNum
            tvFee.text = "나이"
            tvPay.text = item.age//나이가 들어가야함
            tvTeam.text = "가능 시간"
            tvTeamName.text = item.schedule//가능시간이 들어가야함
            tvGender.text = item.gender
            tvViewCount.text = item.viewCount.toString()
            tvNicname.text = item.nicname
            tvContent.text = item.content
            tvTime.text = item.creationTime
            tvLevel.text = item.level
            btnSubmit.setText(R.string.team_detail_application)


        }

        //back button
        btnCancel.setOnClickListener {
            finish() // 현재 Activity 종료
        }

        //submit button
        btnSubmit.setOnClickListener {

        }
    }


}