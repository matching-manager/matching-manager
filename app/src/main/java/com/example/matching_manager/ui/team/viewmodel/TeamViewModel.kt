package com.example.matching_manager.ui.team.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.matching_manager.ui.match.MatchDataModel
import com.example.matching_manager.ui.team.TeamItem
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class TeamViewModel(private val repository: TeamRepository) : ViewModel() {

    private val _list: MutableLiveData<List<TeamItem>> = MutableLiveData()
    val list: MutableLiveData<List<TeamItem>> get() = _list

    private val _realTimeList: MutableLiveData<List<TeamItem>> = MutableLiveData()
    val realTimeList: LiveData<List<TeamItem>> get() = _realTimeList

    private var originalList: MutableList<TeamItem> = mutableListOf() // 원본 데이터를 보관할 리스트


    private val database =
        Firebase.database("https://matching-manager-default-rtdb.asia-southeast1.firebasedatabase.app/")
    private val teamRef = database.getReference("Team")

    private val _event: MutableLiveData<TeamEvent> = MutableLiveData()
    val event: LiveData<TeamEvent> get() = _event

    fun fetchData(
        isRecruitmentChecked: Boolean,
        isApplicationChecked: Boolean,
        game: String?,
        area: String?,
    ) {
//        viewModelScope.launch {
//            val currentList = repository.getList(database)
//            Log.d("MatchViewModel", "fetchData() = currentList : ${currentList.size}")
//
//            originalList.clear()
//            originalList.addAll(currentList)
//            Log.d("MatchViewModel", "game : ${game}" + " area : ${area}")
//            if (game == null && area == null) {
//                when {
//                    isRecruitmentChecked -> filterRecruitmentItems()
//                    isApplicationChecked -> filterApplicationItems()
//                    else -> _list.value = currentList
//                }
//            } else {
//                filterItems(area, game, isRecruitmentChecked, isApplicationChecked)
//
//            }
//        }
        teamRef.addListenerForSingleValueEvent(object : ValueEventListener {
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
                val currentList = dataList
                originalList.clear()
                originalList.addAll(dataList)
                if (game == null && area == null) {
                    when {
                        isRecruitmentChecked -> filterRecruitmentItems()
                        isApplicationChecked -> filterApplicationItems()
                        else -> _list.value = currentList
                    }
                } else {
                    filterItems(area, game, isRecruitmentChecked, isApplicationChecked)

                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // 오류 처리
            }
        })
    }

    fun autoFetchData() {
        teamRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val dataList = mutableListOf<TeamItem>()

                for (childSnapshot in dataSnapshot.children) {
                    val type = childSnapshot.child("type").getValue(String::class.java)
                    if (type == "용병모집") {
                        childSnapshot.getValue(TeamItem.RecruitmentItem::class.java)
                            ?.let { teamData ->
                                dataList.add(teamData)
                            }
                    } else {
                        childSnapshot.getValue(TeamItem.ApplicationItem::class.java)
                            ?.let { teamData ->
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

    //조회수를 업데이트하는 함수
    fun plusViewCount(data: TeamItem) {
        if(data.userId != "로그인한 유저의 아이디") {
            viewModelScope.launch {
                repository.editViewCount(data, database)
            }
        }
    }
    fun plusChatCount(data: TeamItem) {
        viewModelScope.launch {
            repository.editViewCount(data, database)
        }
    }

    fun plusChatCount(data: TeamItem) {
        viewModelScope.launch {
            repository.editViewCount(data, database)
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
        _list.value = originalList.filterIsInstance<TeamItem.RecruitmentItem>()
    }

    //용병신청만 필터링하는 함수
    fun filterApplicationItems() {
        _list.value = originalList.filterIsInstance<TeamItem.ApplicationItem>()
    }

    fun filterItems(
        area: String?,
        game: String?,
        isRecruitmentChecked: Boolean,
        isApplicationChecked: Boolean,
    ) {
        val filteredList = mutableListOf<TeamItem>()

        if (isRecruitmentChecked && !isApplicationChecked) {
            // 용병모집 버튼만 체크된 경우
            filteredList.addAll(originalList.filterIsInstance<TeamItem.RecruitmentItem>())
        } else if (!isRecruitmentChecked && isApplicationChecked) {
            // 용병신청 버튼만 체크된 경우
            filteredList.addAll(originalList.filterIsInstance<TeamItem.ApplicationItem>())
        } else {
            // 둘 다 체크되지 않은 경우
            filteredList.addAll(originalList)
        }

        val result = filteredList.filter { item ->
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
                isGameMatched && isAreaMatched
            }
        }
        Log.d("filter match", "${filteredList}")
        _list.value = result
    }

    fun clearFilter() {
        _list.value = originalList // 현재 _list에 있는 값을 다시 할당하여 불러오기
    }
}

sealed interface TeamEvent {
    object Finish : TeamEvent
}


