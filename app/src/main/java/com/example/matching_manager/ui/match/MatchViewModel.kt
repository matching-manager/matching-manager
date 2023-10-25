package com.example.matching_manager.ui.match

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MatchViewModel(private val repository: MatchRepository) : ViewModel() {

    private val _list: MutableLiveData<List<MatchDataModel>> = MutableLiveData()
    val list: LiveData<List<MatchDataModel>> get() = _list

    private val _event: MutableLiveData<MatchEvent> = MutableLiveData()
    val event: LiveData<MatchEvent> get() = _event

    private val database = Firebase.database("https://matching-manager-default-rtdb.asia-southeast1.firebasedatabase.app/")
    fun fetchData() {
        viewModelScope.launch {
            val currentList = repository.getList(database)
            Log.d("MatchViewModel", "fetchData() = currentList : ${currentList.size}")

            _list.postValue(currentList)
        }
    }

    fun addMatch(data: MatchDataModel) {
        viewModelScope.launch {
            repository.addData(data, database)
            _event.postValue(MatchEvent.Finish)
        }
    }
}
sealed interface MatchEvent {
    object Finish : MatchEvent
}