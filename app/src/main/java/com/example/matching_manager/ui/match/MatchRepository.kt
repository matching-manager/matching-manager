package com.example.matching_manager.ui.match

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

interface MatchRepository {
    fun getList(): LiveData<MutableList<MatchDataModel>>
    fun addData(data : MatchDataModel) {
    }
}