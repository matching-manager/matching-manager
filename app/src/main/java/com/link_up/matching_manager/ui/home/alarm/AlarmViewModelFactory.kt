package com.link_up.matching_manager.ui.home.alarm

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.link_up.matching_manager.data.repository.SharedPreferenceRepositoryImpl
import com.link_up.matching_manager.domain.repository.SharedPreferenceRepository
import com.link_up.matching_manager.domain.usecase.sharedpreference.LoadFcmDataUseCase

class AlarmViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    private val repository: SharedPreferenceRepository = SharedPreferenceRepositoryImpl()

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AlarmViewModel::class.java)) {
            return AlarmViewModel(
                LoadFcmDataUseCase(repository),
                context
            ) as T
        } else {
            throw IllegalArgumentException("Not found ViewModel class.")
        }
    }
}