package com.example.matching_manager.ui.my

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MySharedViewModel : ViewModel() {
    private val _number: MutableLiveData<Int> = MutableLiveData()
    private val _teamTime = MutableLiveData<Triple<Int, Int, String>>() // 시간, 분, 오전/오후
    private val _calendar = MutableLiveData<Quad<Int, Int, Int, String>>() // 년,월,일,요일


    val number: LiveData<Int> get() = _number
    val teamTime: LiveData<Triple<Int, Int, String>> get() = _teamTime
    val calendar: LiveData<Quad<Int, Int, Int, String>> get() = _calendar

    fun updateTeamNumber(num: Int) {
        _number.value = num
    }

    fun updateTeamTime(hour: Int, minute: Int, amPm: String) {
        _teamTime.value = Triple(hour, minute, amPm)
    }

    fun updateCalendar(year: Int, month: Int, dayOfMonth: Int, dayOfWeek: String) {
        _calendar.value = Quad(year, month, dayOfMonth, dayOfWeek)
    }


}

//임시
data class Quad<out A, out B, out C, out D>(
    val first: A,
    val second: B,
    val third: C,
    val fourth: D
)
