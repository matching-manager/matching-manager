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


    fun filterItems(area: String?, game: String?) {
        if ("선택" in game.orEmpty() && "선택" in area.orEmpty()) {
            // 게임과 지역이 선택되지 않았을 경우, 필터링을 하지 않고 모든 아이템을 보여줍니다.
            _list.value = originalList
            return
        }
        val filteredList = originalList.filter { item ->
            // 선택된 게임 또는 지역이 있을 경우, 해당 조건에 맞는 아이템만 필터링합니다.
            val isGameMatched = game.isNullOrBlank() || (item.game.contains(
                game,
                ignoreCase = true
            ))
            val isAreaMatched = area.isNullOrBlank() || (item.matchPlace.contains(
                area,
                ignoreCase = true
            ))
            if (area!!.contains("선택")) {
                isGameMatched
            } else if (game!!.contains("선택")) {
                isAreaMatched
            } else {
                //둘 다 체크되었을때
                isGameMatched && isAreaMatched
            }
        }
        _list.value = filteredList

    }
}
sealed interface MatchEvent {
    object Finish : MatchEvent
}