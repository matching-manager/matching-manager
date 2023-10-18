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
        initFcm()
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


        }

        //back button
        btnCancel.setOnClickListener {
            finish() // 현재 Activity 종료
        }

        //submit button
        btnSubmit.setOnClickListener {

        }
    }

    private fun initFcm() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // 알림을 표시할 채널 생성
            val channelId = getString(R.string.default_notification_channel_id)
            val channelName = getString(R.string.default_notification_channel_name)
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager?.createNotificationChannel(
                NotificationChannel(
                    channelId,
                    channelName,
                    NotificationManager.IMPORTANCE_LOW,
                ),
            )
        }

        // 알림 메시지가 탭될 때, 알림 메시지와 함께 제공된 데이터는 인텐트 익스트라에서 사용 가능
        // 예제에서는 런처 인텐트가 알림이 탭될 때 시작되므로, 함께 제공된 데이터는 여기서 처리
        // 다른 인텐트를 시작하려면 알림 메시지의 click_action 필드를 원하는 인텐트로 설정하면 됨
        intent.extras?.let {
            for (key in it.keySet()) {
                val value = intent.extras?.getString(key)
                Log.d(TAG, "키: $key 값: $value")
            }
        }

//        binding.subscribeButton.setOnClickListener {
//            Log.d(TAG, "날씨 주제에 가입 중")
//            // 날씨 주제에 가입
//            Firebase.messaging.subscribeToTopic("weather")
//                .addOnCompleteListener { task ->
//                    var msg = getString(R.string.msg_subscribed)
//                    if (!task.isSuccessful) {
//                        msg = getString(R.string.msg_subscribe_failed)
//                    }
//                    Log.d(TAG, msg)
//                    Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
//                }
//        }

        binding.btnSubmit.setOnClickListener {
            // 토큰 가져오기
            Firebase.messaging.token.addOnCompleteListener(
                OnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        Log.w(TAG, "FCM 등록 토큰 가져오기 실패", task.exception)
                        return@OnCompleteListener
                    }

                    // 새 FCM 등록 토큰 가져오기
                    val token = task.result

                    // 로깅 및 토스트
                    val msg = getString(R.string.msg_token_fmt, token)
                    Log.d(TAG, msg)
                    Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
                    Log.w(TAG, msg, task.exception)

                },
            )
        }
        askNotificationPermission()
    }
    private fun askNotificationPermission() {
        // API 레벨이 33보다 큰 경우에만 필요
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                // FCM SDK (및 앱)은 알림을 게시할 수 있음
            } else {
                // 권한 직접 요청
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    // 권한 요청 결과를 처리하는 런처
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { isGranted: Boolean ->
        if (isGranted) {
            Toast.makeText(this, "알림 권한이 허용되었습니다", Toast.LENGTH_SHORT)
                .show()
        } else {
            Toast.makeText(
                this,
                "POST_NOTIFICATIONS 권한 없이 FCM 알림을 게시할 수 없습니다",
                Toast.LENGTH_LONG,
            ).show()
        }
    }
}