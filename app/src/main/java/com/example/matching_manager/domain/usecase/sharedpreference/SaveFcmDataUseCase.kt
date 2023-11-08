package com.example.matching_manager.domain.usecase.sharedpreference

import android.content.Context
import com.example.matching_manager.domain.repository.SharedPreferenceRepository
import com.example.matching_manager.ui.home.alarm.AlarmModel

class SaveFcmDataUseCase (
    private val repository: SharedPreferenceRepository
) {
    operator fun invoke(
        context: Context,
        list : List<AlarmModel>
    ) = repository.saveFcmData(
        context,
        list
    )
}