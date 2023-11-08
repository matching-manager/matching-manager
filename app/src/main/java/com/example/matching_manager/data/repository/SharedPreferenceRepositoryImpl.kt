package com.example.matching_manager.data.repository

import android.content.Context
import com.example.matching_manager.domain.repository.SharedPreferenceRepository
import com.example.matching_manager.ui.home.alarm.AlarmModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SharedPreferenceRepositoryImpl: SharedPreferenceRepository {
    private val PREFERENCE_FCM_KEY = "preference_key"
    private val PREFERENCE_USER_TYPE_KEY = "user_type_key"

    // 리스트의 형태로 데이터 저장
    override fun saveFcmData(context: Context, values: List<AlarmModel>) {
        val gson = Gson()
        val json = gson.toJson(values)
        val prefs = context.getSharedPreferences("SETTINGS", Context.MODE_PRIVATE)
        val editor = prefs?.edit()
        editor?.putString(PREFERENCE_FCM_KEY, json)
        editor?.apply()
    }

    // 데이터 로드
    override fun loadFcmData(context: Context): List<AlarmModel> {
        val prefs = context.getSharedPreferences("SETTINGS", Context.MODE_PRIVATE)
        val json = prefs?.getString(PREFERENCE_FCM_KEY, null)

        return if (json != null) {
            val gson = Gson()
            val storedData: List<AlarmModel> =
                gson.fromJson(json, object : TypeToken<List<AlarmModel>>() {}.type)
            storedData
        } else {
            emptyList() // null일 때 빈 리스트 반환
        }
    }

    // 자동 로그인용 Key
    override fun saveUserType(context: Context, check: Boolean) {
        val prefs = context.getSharedPreferences("SETTINGS", Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putBoolean(PREFERENCE_USER_TYPE_KEY, check)
        editor.apply()
    }

    // 사용자 유형 확인
    override fun checkUserType(context: Context): Boolean {
        val prefs = context.getSharedPreferences("SETTINGS", Context.MODE_PRIVATE)
        return prefs.getBoolean(PREFERENCE_USER_TYPE_KEY, false) // 기본값으로 false를 반환
    }
}