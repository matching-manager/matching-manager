package com.link_up.matching_manager.ui.match

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MatchSharedViewModel : ViewModel() {
    private val _number: MutableLiveData<Int> = MutableLiveData()
    private val _teamTime = MutableLiveData<Triple<Int, Int, String>>() // 시간, 분, 오전/오후
    private val _calendar = MutableLiveData<Quad<Int, Int, Int, String>>() // 년,월,일,요일
    private val _filter = MutableLiveData<Pair<String, String>>() //지역,경기

    val number: LiveData<Int> get() = _number
    val teamTime: LiveData<Triple<Int, Int, String>> get() = _teamTime
    val calendar: LiveData<Quad<Int, Int, Int, String>> get() = _calendar
    val filter: LiveData<Pair<String, String>> get() = _filter

    fun updateTeamNumber(num: Int) {
        _number.value = num
    }

    fun updateTeamTime(hour: Int, minute: Int, amPm: String) {
        _teamTime.value = Triple(hour, minute, amPm)
    }

    fun updateCalendar(year: Int, month: Int, dayOfMonth: Int, dayOfWeek: String) {
        _calendar.value = Quad(year, month, dayOfMonth, dayOfWeek)
    }

    fun updateFilter(area: String, game: String) {
        _filter.value = Pair(area,game)
    }


}

//임시
data class Quad<out A, out B, out C, out D>(
    val first: A,
    val second: B,
    val third: C,
    val fourth: D
)
