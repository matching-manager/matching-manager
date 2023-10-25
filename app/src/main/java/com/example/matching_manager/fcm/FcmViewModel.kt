package com.example.matching_manager.fcm

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.matching_manager.domain.usecase.sharedpreference.LoadFcmDataUseCase
import com.example.matching_manager.domain.usecase.sharedpreference.SaveFcmDataUseCase
import com.example.matching_manager.ui.home.arena.alarm.AlarmModel

class FcmViewModel(
    private val saveFcmList: SaveFcmDataUseCase,
    private val loadFcmList: LoadFcmDataUseCase,
    private val context: Context
) : ViewModel() {
    private val _list: MutableLiveData<List<AlarmModel>> = MutableLiveData()
    val list: LiveData<List<AlarmModel>> get() = _list

    fun addFcmAlarm(userId: String?, phoneNumber: String?, body: String?) {
        val currentList = loadFcmData()
        currentList.add(
            AlarmModel(
                userId = userId,
                body = body,
                phoneNumber = phoneNumber
            )
        )
        saveFcmList(context,currentList)
        _list.value = currentList
    }

    private fun loadFcmData() = loadFcmList(context).toMutableList()
}

//sealed interface FcmState {
//    data class AddFcm(val alarmList: List<AlarmModel>) : FcmState
//    data class LoadFcm(val alarmList: List<AlarmModel>) : FcmState
//}