package com.link_up.matching_manager.domain.usecase.sharedpreference

import android.content.Context
import com.link_up.matching_manager.domain.repository.SharedPreferenceRepository
import com.link_up.matching_manager.ui.home.alarm.AlarmModel

class LoadFcmDataUseCase(
    private val repository: SharedPreferenceRepository
) {
    operator fun invoke(
        context: Context
    ): List<AlarmModel> = repository.loadFcmData(
        context
    )
}