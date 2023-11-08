package com.example.matching_manager.domain.usecase.sharedpreference

import android.content.Context
import com.example.matching_manager.domain.model.ArenaEntity
import com.example.matching_manager.domain.repository.SharedPreferenceRepository
import com.example.matching_manager.ui.home.alarm.AlarmModel

class LoadFcmDataUseCase(
    private val repository: SharedPreferenceRepository
) {
    operator fun invoke(
        context: Context
    ): List<AlarmModel> = repository.loadFcmData(
        context
    )
}