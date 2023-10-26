package com.example.matching_manager.util

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.matching_manager.ui.home.arena.alarm.AlarmModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.Locale

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

}