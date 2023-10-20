package com.example.matching_manager.ui.team.view

import android.icu.text.Transliterator.Position
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.matching_manager.ui.team.TeamItem

class TeamSharedViewModel : ViewModel() {
    private val _number: MutableLiveData<Int> = MutableLiveData()
    private val _age: MutableLiveData<Int> = MutableLiveData()
    val number: LiveData<Int> get() = _number
    val age: LiveData<Int> get() = _age

    fun updateTeamNumber(num: Int) {
        _number.value = num
    }

    fun updateTeamAge(age: Int) {
        _age.value = age
    }

}