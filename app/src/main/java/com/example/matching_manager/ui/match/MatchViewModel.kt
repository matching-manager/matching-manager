package com.example.matching_manager.ui.match

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class MatchViewModel(private val repository: MatchRepository) : ViewModel() {

    private val _list: MutableLiveData<List<MatchDataModel>> = MutableLiveData()
    private var originalList: MutableList<MatchDataModel> = mutableListOf() // 원본 데이터를 보관할 리스트
    val list: LiveData<List<MatchDataModel>> get() = _list


    private val _event: MutableLiveData<MatchEvent> = MutableLiveData()
    val event: LiveData<MatchEvent> get() = _event

    private val database = Firebase.database("https://matching-manager-default-rtdb.asia-southeast1.firebasedatabase.app/")
    fun fetchData() {
        viewModelScope.launch {
            val currentList = repository.getList(database)
            Log.d("MatchViewModel", "fetchData() = currentList : ${currentList.size}")
            originalList = currentList.toMutableList()
            _list.postValue(currentList)
        }
    }

    fun addMatch(data: MatchDataModel) {
        viewModelScope.launch {
            repository.addData(data, database)
            _event.postValue(MatchEvent.Finish)
        }
    }

    fun filterItems(selectedGame: String?, selectedArea: String?) {
        if ("선택" in selectedGame.orEmpty() && "선택" in selectedArea.orEmpty()) {
            // 게임과 지역이 선택되지 않았을 경우, 필터링을 하지 않고 모든 아이템을 보여줍니다.
            _list.value = originalList
            return
        }
        val filteredList = originalList.filter { item ->
            // 선택된 게임 또는 지역이 있을 경우, 해당 조건에 맞는 아이템만 필터링합니다.
            val isGameMatched = selectedGame.isNullOrBlank() || (item.game.contains(
                selectedGame,
                ignoreCase = true
            ))
            val isAreaMatched = selectedArea.isNullOrBlank() || (item.matchPlace.contains(
                selectedArea,
                ignoreCase = true
            ))
            isGameMatched || isAreaMatched
        }
        _list.value = filteredList
    }
}
sealed interface MatchEvent {
    object Finish : MatchEvent
}