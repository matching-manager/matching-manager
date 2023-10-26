package com.example.matching_manager.domain.repository

import android.content.Context
import com.example.matching_manager.ui.home.arena.alarm.AlarmModel

interface SharedPreferenceRepository {
    fun saveFcmData(context: Context, values: List<AlarmModel>)
    fun loadFcmData(context: Context): List<AlarmModel>
}