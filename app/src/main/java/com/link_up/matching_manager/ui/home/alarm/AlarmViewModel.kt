package com.link_up.matching_manager.ui.home.alarm

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.link_up.matching_manager.domain.usecase.sharedpreference.LoadFcmDataUseCase

class AlarmViewModel(
    private val loadAlarmList: LoadFcmDataUseCase,
    private val context: Context
) : ViewModel() {
    private val _list: MutableLiveData<List<AlarmModel>> = MutableLiveData()
    val list: LiveData<List<AlarmModel>> get() = _list

    fun loadAlarm() {
        _list.value = loadFcmData()
        Log.d("????","${_list.value}")
    }

    private fun loadFcmData() = loadAlarmList(context)
//    fun removeAlarm(){
//        val currentList = list.value.orEmpty().toMutableList()
//        currentList.clear()
//        _list.value = currentList
//    }

}