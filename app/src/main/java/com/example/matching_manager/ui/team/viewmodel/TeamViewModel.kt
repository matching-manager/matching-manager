package com.example.matching_manager.ui.team.viewmodel

import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.matching_manager.R
import com.example.matching_manager.ui.team.TeamAddType
import com.example.matching_manager.ui.match.MatchDataModel
import com.example.matching_manager.ui.match.MatchEvent
import com.example.matching_manager.ui.team.TeamItem
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class TeamViewModel(private val repository: TeamRepository) : ViewModel() {

    private val _list: MutableLiveData<List<TeamItem>> = MutableLiveData()
    private var originalList: MutableList<TeamItem> = mutableListOf() // 원본 데이터를 보관할 리스트
    val list: MutableLiveData<List<TeamItem>> get() = _list

    private val _realTimeList: MutableLiveData<List<TeamItem>> = MutableLiveData()
    val realTimeList: LiveData<List<TeamItem>> get() = _realTimeList

    private var isRecruitmentChecked = false
    private var isApplicationChecked = false

    private val database = Firebase.database("https://matching-manager-default-rtdb.asia-southeast1.firebasedatabase.app/")
    private val teamRef = database.getReference("Team")

    private val _event: MutableLiveData<TeamEvent> = MutableLiveData()
    val event: LiveData<TeamEvent> get() = _event

    fun fetchData() {
        viewModelScope.launch {
            val currentList = repository.getList(database)
            Log.d("MatchViewModel", "fetchData() = currentList : ${currentList.size}")

            originalList.clear()
            originalList.addAll(currentList)
            _list.value = originalList
        }
    }

    fun autoFetchData() {
        teamRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val dataList = mutableListOf<TeamItem>()

                for (childSnapshot in dataSnapshot.children) {
                    val type = childSnapshot.child("type").getValue(String::class.java)
                    if (type == "용병모집") {
                        childSnapshot.getValue(TeamItem.RecruitmentItem::class.java)?.let { teamData ->
                            dataList.add(teamData)
                        }
                    }
                    else {
                        childSnapshot.getValue(TeamItem.ApplicationItem::class.java)?.let { teamData ->
                            dataList.add(teamData)
                        }
                    }
                }
                _realTimeList.value = dataList
                Log.d("autoFetchData", "${_realTimeList.value}")
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // 오류 처리
            }
        })
    }

    fun addRecruitment(data: TeamItem.RecruitmentItem) {
        viewModelScope.launch {
            repository.addRecruitmentData(data, database)
            _event.postValue(TeamEvent.Finish)
        }
    }

    fun addApplication(data: TeamItem.ApplicationItem) {
        viewModelScope.launch {
            repository.addApplicationData(data, database)
            _event.postValue(TeamEvent.Finish)
        }
    }

    // 용병모집만 필터링하는 함수
    fun filterRecruitmentItems() {
        isRecruitmentChecked = true
        if (isRecruitmentChecked) {
            _list.value = originalList.filterIsInstance<TeamItem.RecruitmentItem>()
        }
    }

    //용병신청만 필터링하는 함수
    fun filterApplicationItems() {
        isApplicationChecked = true
        if (isApplicationChecked) {
            _list.value = originalList.filterIsInstance<TeamItem.ApplicationItem>()
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
            val isAreaMatched = area.isNullOrBlank() || (item.area.contains(
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
        Log.d("filter match","${filteredList}")
        _list.value = filteredList

    }

    fun clearFilter() {
        _list.value = originalList // 현재 _list에 있는 값을 다시 할당하여 불러오기
    }
}

sealed interface TeamEvent {
    object Finish : TeamEvent
}


