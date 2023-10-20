package com.example.matching_manager.ui.match

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MatchViewModel(private val repository: MatchRepository) : ViewModel() {

    private val _list: MutableLiveData<List<MatchDataModel>> = MutableLiveData()
    val list: LiveData<List<MatchDataModel>> get() = _list

    private val _event: MutableLiveData<MatchEvent> = MutableLiveData()
    val event: LiveData<MatchEvent> get() = _event
    fun fetchData() {
        viewModelScope.launch {
            val currentList = repository.getList()
            Log.d("MatchViewModel", "fetchData() = currentList : ${currentList.size}")

            _list.postValue(currentList)
        }
    }

    fun addMatch(data: MatchDataModel) {
        viewModelScope.launch {
            repository.addData(data)
            _event.postValue(MatchEvent.Finish)
        }

    }
}
sealed interface MatchEvent {
    object Finish : MatchEvent
}