package com.link_up.matching_manager.util

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.link_up.matching_manager.data.room.ApplicationEntity
import com.link_up.matching_manager.data.room.MatchEntity
import com.link_up.matching_manager.data.room.RecruitmentEntity
import com.link_up.matching_manager.ui.match.MatchDataModel
import com.link_up.matching_manager.ui.team.TeamItem

object Utils {

    private const val REQUEST_PHONE_CALL = 1

    fun shareUrl(context: Context, url : String){
        val share = Intent.createChooser(Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, url)
            // (Optional) Here we're setting the title of the content
            putExtra(Intent.EXTRA_TITLE, "Share Place URL")
            // (Optional) Here we're passing a content URI to an image to be displayed
            data = Uri.parse(url)
            type = "text/plain"
            flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        }, null)
        context.startActivity(share)
    }

    fun callPhoneNumber(activity : Activity, phoneNumber: String) {
        Log.d("test" ,"phone = $phoneNumber")
        val callIntent = Intent(Intent.ACTION_CALL)
        callIntent.data = Uri.parse("tel:$phoneNumber")

        if (ContextCompat.checkSelfPermission(
                activity,
                Manifest.permission.CALL_PHONE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(Manifest.permission.CALL_PHONE),
                REQUEST_PHONE_CALL
            )
            return
        }
        activity.startActivity(callIntent)
    }

    fun messagePhoneNumber(activity: Activity, phoneNumber: String){
        val smsUri = Uri.parse("smsto:$phoneNumber") // 문자를 전송할 phoneNumber
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data = smsUri
        intent.putExtra("sms_body", "") // body에 전송할 내용입니다.
        activity.startActivity(intent)
    }

    fun MatchEntity.toMatchDataModel(): MatchDataModel {
        return MatchDataModel(
            matchId,
            userId,
            userNickname,
            userImg,
            userEmail,
            fcmToken,
            phoneNum,
            teamName,
            game,
            schedule,
            matchPlace,
            playerNum,
            entryFee,
            description,
            gender,
            level,
            postImg,
            viewCount,
            chatCount,
            uploadTime
        )
    }

    fun MatchDataModel.toMatchEntity(): MatchEntity {
        return MatchEntity(
            matchId,
            userId,
            userNickname,
            userImg,
            userEmail,
            fcmToken,
            phoneNum,
            teamName,
            game,
            schedule,
            matchPlace,
            playerNum,
            entryFee,
            description,
            gender,
            level,
            postImg,
            viewCount,
            chatCount,
            uploadTime
        )
    }

    fun TeamItem.RecruitmentItem.toRecruitmentEntity(): RecruitmentEntity {
        return RecruitmentEntity(
            type,
            teamId,
            userId,
            nickname,
            userImg,
            userEmail,
            phoneNum,
            fcmToken,
            description,
            gender,
            chatCount,
            level,
            playerNum,
            postImg,
            schedule,
            uploadTime,
            viewCount,
            game,
            area,
            pay,
            teamName
        )
    }

    fun TeamItem.ApplicationItem.toApplicationEntity(): ApplicationEntity {
        return ApplicationEntity(
            type,
            teamId,
            userId,
            nickname,
            userImg,
            userEmail,
            phoneNum,
            fcmToken,
            description,
            gender,
            chatCount,
            level,
            playerNum,
            postImg,
            schedule,
            uploadTime,
            viewCount,
            game,
            area,
            age
        )
    }

    fun RecruitmentEntity.toRecruitment(): TeamItem.RecruitmentItem {
        return TeamItem.RecruitmentItem(
            type,
            teamId,
            userId,
            nickname,
            userImg,
            userEmail,
            phoneNum,
            fcmToken,
            description,
            gender,
            chatCount,
            level,
            playerNum,
            postImg,
            schedule,
            uploadTime,
            viewCount,
            game,
            area,
            pay,
            teamName
        )
    }

    fun ApplicationEntity.toApplication(): TeamItem.ApplicationItem {
        return TeamItem.ApplicationItem(
            type,
            teamId,
            userId,
            nickname,
            userImg,
            userEmail,
            phoneNum,
            fcmToken,
            description,
            gender,
            chatCount,
            level,
            playerNum,
            postImg,
            schedule,
            uploadTime,
            viewCount,
            game,
            area,
            age
        )
    }
}