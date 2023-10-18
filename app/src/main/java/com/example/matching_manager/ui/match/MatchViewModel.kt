package com.example.matching_manager.ui.match

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MatchViewModel(private val repository: MatchRepository) : ViewModel() {

    private val _list: MutableLiveData<List<MatchDataModel>> = MutableLiveData()
    val list: LiveData<List<MatchDataModel>> get() = _list

    var matchId = 25

    suspend fun fetchData() {

        val currentList =withContext(Dispatchers.IO) {
            repository.getList()
        }
        Log.d("MatchViewModel", "fetchData() = currentList : ${currentList.size}")

        _list.value = currentList

    }

    suspend fun addMatch(data: MatchDataModel) {
        repository.addData(data)
    }
}