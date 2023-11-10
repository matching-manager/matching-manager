package com.link_up.matching_manager.ui.calender

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.link_up.matching_manager.data.repository.SharedPreferenceRepositoryImpl
import com.link_up.matching_manager.domain.repository.SharedPreferenceRepository
import com.link_up.matching_manager.domain.usecase.sharedpreference.LoadCalendarDataUseCase
import com.link_up.matching_manager.domain.usecase.sharedpreference.SaveCalendarDataUseCase

class CalendarViewModelFactory(val context: Context) : ViewModelProvider.Factory {

    private val repository : SharedPreferenceRepository = SharedPreferenceRepositoryImpl()

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CalendarViewModel::class.java)) {
            return CalendarViewModel(
                SaveCalendarDataUseCase(repository),
                LoadCalendarDataUseCase(repository),
                context
            ) as T
        } else {
            throw IllegalArgumentException("Not found ViewModel class.")
        }
    }
}