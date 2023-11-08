package com.link_up.matching_manager.domain.repository

import android.content.Context
import com.link_up.matching_manager.ui.home.alarm.AlarmModel

interface SharedPreferenceRepository {
    fun saveFcmData(context: Context, values: List<AlarmModel>)
    fun loadFcmData(context: Context): List<AlarmModel>
    fun saveUserType(context: Context, check: Boolean)
    fun checkUserType(context: Context): Boolean
}