package com.example.matching_manager.ui.home.alarm

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AlarmViewModel : ViewModel() {
    private val _list: MutableLiveData<List<AlarmModel>> = MutableLiveData()
    val list: LiveData<List<AlarmModel>> get() = _list

    fun addAlarm(userId: String?, body: String?, userPhoneNumber: String?) {

        val currentList = list.value.orEmpty().toMutableList()
        currentList.add(AlarmModel(
            userId=userId,
            body = body,
            phoneNumber = userPhoneNumber
        ))
        _list.value = currentList
        Log.d("AlarmViewModel","$currentList")
    }
    fun removeAlarm(){
        val currentList = list.value.orEmpty().toMutableList()
        currentList.clear()
        _list.value = currentList
    }

}