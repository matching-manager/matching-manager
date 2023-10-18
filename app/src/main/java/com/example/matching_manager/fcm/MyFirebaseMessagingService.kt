package com.example.matching_manager.fcm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.example.matching_manager.R
import com.example.matching_manager.ui.home.alarm.AlarmActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    /**
     * FCM 메시지 수신 시 호출됩니다.
     *
     * @param remoteMessage Firebase Cloud Messaging에서 수신한 메시지를 나타내는 객체입니다.
     */
    // [START receive_message]
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d(TAG, "received!!!")
        // [START_EXCLUDE]
        // 데이터 메시지와 알림 메시지 두 가지 유형의 메시지가 있습니다.
        // 데이터 메시지는 앱이 포그라운드 또는 백그라운드에서 상관없이
        // 여기서 처리됩니다. 알림 메시지는 앱이 포그라운드에서만 수신됩니다.
        // 백그라운드에서 알림 메시지가 수신되면 자동으로 생성된 알림이 표시됩니다.
        // 사용자가 알림을 탭하면 앱으로 돌아갑니다.
        // 알림과 데이터 페이로드가 모두 포함된 메시지는 알림 메시지로 처리됩니다.
        // Firebase 콘솔은 항상 알림 메시지를 보냅니다.
        // 자세한 내용은 다음을 참조하세요: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]

        // TODO(developer): FCM 메시지 처리
        // 메시지를 받지 못하나요? 이런 경우를 확인하세요: https://goo.gl/39bRNJ
        Log.d(TAG, "발신자: ${remoteMessage.from}")

        // 메시지가 데이터 페이로드를 포함하는지 확인합니다.
        if (remoteMessage.data.isNotEmpty()) {
            Log.d(TAG, "메시지 데이터 페이로드: ${remoteMessage.data}")

            handleNow()

//            // 데이터가 긴 작업으로 처리해야 하는지 확인합니다.
//            if (isLongRunningJob()) {
//                // 긴 작업 (10초 이상)의 경우 WorkManager를 사용합니다.
//                scheduleJob()
//            } else {
//                // 10초 내에 메시지 처리
//                handleNow()
//            }
        }

        // 메시지가 알림 페이로드를 포함하는지 확인합니다.
        remoteMessage.notification?.let {
            Log.d(TAG, "메시지 알림 본문: ${it.body}")
            it.body?.let { body -> sendNotification(remoteMessage,body) }
        }

        // 또한, FCM 메시지를 수신한 결과로 알림을 생성하려면 여기에서 시작해야 합니다.
    }
    // [END receive_message]

    private fun isLongRunningJob() = true

    // [START on_new_token]
    /**
     * FCM 등록 토큰이 업데이트되면 호출됩니다.
     * 이전 토큰의 보안이 손상된 경우 발생할 수 있습니다.
     * 이 메서드는 FCM 등록 토큰이 처음 생성될 때도 호출됩니다.
     * 여기서 토큰을 검색해야 합니다.
     */
    override fun onNewToken(token: String) {
        Log.d(TAG, "새로 고친 토큰: $token")

        // 이 애플리케이션 인스턴스로 메시지를 보내거나
        // 서버 측에서 이 애플리케이션의 구독을 관리하려면
        // FCM 등록 토큰을 앱 서버로 보내야 합니다.
        sendRegistrationToServer(token)
    }
    // [END on_new_token]


//    /**
//     * WorkManager를 사용하여 비동기 작업 예약
//     */
//    private fun scheduleJob() {
//        // [START dispatch_job]
//        val work = OneTimeWorkRequest.Builder(MyWorker::class.java).build()
//        WorkManager.getInstance(this).beginWith(work).enqueue()
//        // [END dispatch_job]
//    }


    /**
     * BroadcastReceivers에 할당된 시간을 처리합니다.
     */
    private fun handleNow() {
        Log.d(TAG, "단기 작업이 완료되었습니다.")
    }

    /**
     * 등록 토큰을 서버에 보관합니다.
     *
     * 이 메서드를 수정하여 사용자의 FCM 등록 토큰을 앱에서 유지 관리하는
     * 서버 측 계정과 연결합니다.
     *
     * @param token 새로운 토큰
     */
    private fun sendRegistrationToServer(token: String?) {
        // TODO: 이 메서드를 구현하여 토큰을 앱 서버로 전송합니다.
        Log.d(TAG, "sendRegistrationTokenToServer($token)")
    }

    /**
     * 수신한 FCM 메시지의 본문을 포함한 간단한 알림을 만들어 표시합니다.
     *
     * @param messageBody 수신한 FCM 메시지 본문입니다.
     */
    private fun sendNotification(remoteMessage:RemoteMessage,messageBody: String) {

        val phoneNumber = remoteMessage.data["phone_number"]
        val userId = remoteMessage.data["user_id"]

        Log.d(TAG, "send_notification_phone_number : $phoneNumber")
        Log.d(TAG, "send_notification_user_id : $userId")

        val requestCode = 0
        val intent = Intent(this, AlarmActivity::class.java).apply {
            putExtra(RECEIVED_USER_PHONE_NUMBER, phoneNumber)
            putExtra(RECEIVED_USER_ID, userId)
            putExtra(RECEIVED_BODY, messageBody)
//            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        val pendingIntent = PendingIntent.getActivity(
            this,
            requestCode,
            intent,
            PendingIntent.FLAG_IMMUTABLE,
        )

        val channelId = getString(R.string.default_notification_channel_id)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_stat_ic_notification)
            .setContentTitle(getString(R.string.fcm_message))
            .setContentText(messageBody)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Android Oreo 이상에서는 알림 채널이 필요합니다.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Channel human readable title",
                NotificationManager.IMPORTANCE_DEFAULT,
            )
            notificationManager.createNotificationChannel(channel)
        }

        val notificationId = 0
        notificationManager.notify(notificationId, notificationBuilder.build())
    }

    companion object {
        private const val TAG = "MyFirebaseMsgService"
        const val RECEIVED_USER_ID = "received_user_id"
        const val RECEIVED_USER_PHONE_NUMBER = "received_user_phone_number"
        const val RECEIVED_BODY = "received_body"
    }
}
/**
"notificationBuilder" 알림 생성시 여러가지 옵션을 이용해 커스텀 가능.
setSmallIcon : 작은 아이콘 (필수)
setContentTitle : 제목 (필수)
setContentText : 내용 (필수)
setColor : 알림내 앱 이름 색
setWhen : 받은 시간 커스텀 ( 기본 시스템에서 제공합니다 )
setShowWhen : 알림 수신 시간 ( default 값은 true, false시 숨길 수 있습니다 )
setOnlyAlertOnce : 알림 1회 수신 ( 동일 아이디의 알림을 처음 받았을때만 알린다, 상태바에 알림이 잔존하면 무음 )
setContentTitle : 제목
setContentText : 내용
setFullScreenIntent : 긴급 알림 ( 자세한 설명은 아래에서 설명합니다 )
setTimeoutAfter : 알림 자동 사라지기 ( 지정한 시간 후 수신된 알림이 사라집니다 )
setContentIntent : 알림 클릭시 이벤트 ( 지정하지 않으면 클릭했을때 아무 반응이 없고 setAutoCancel 또한 작동하지 않는다 )
setLargeIcon : 큰 아이콘 ( mipmap 에 있는 아이콘이 아닌 drawable 폴더에 있는 아이콘을 사용해야 합니다. )
setAutoCancel : 알림 클릭시 삭제 여부 ( true = 클릭시 삭제 , false = 클릭시 미삭제 )
setPriority : 알림의 중요도를 설정 ( 중요도에 따라 head up 알림으로 설정할 수 있는데 자세한 내용은 밑에서 설명하겠습니다. )
setVisibility : 잠금 화면내 알림 노출 여부
Notification.VISIBILITY_PRIVATE : 알림의 기본 정보만 노출 (제목, 타이틀 등등)
Notification.VISIBILITY_PUBLIC : 알림의 모든 정보 노출
Notification.VISIBILITY_SECRET : 알림의 모든 정보 비노출
 */

