package com.example.matching_manager.ui.match

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MatchViewModel(private val repository: MatchRepository) : ViewModel() {

    private val _list : MutableLiveData<List<MatchDataModel>> = MutableLiveData()
    val list : LiveData<List<MatchDataModel>> get() = _list

    var matchId = 25

    fun fetchData() {
        repository.getList().observeForever{
            val currentList = it
            Log.d("MatchViewModel", "fetchData() = currentList : ${currentList.size}")

            _list.value = currentList
        }
    }

    fun addMatch(data : MatchDataModel) {
        repository.addData(data)
    }
}