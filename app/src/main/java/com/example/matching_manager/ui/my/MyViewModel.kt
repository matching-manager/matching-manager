package com.example.matching_manager.ui.my

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
class MyViewModel(private val repository: MyMatchRepository) : ViewModel() {

    private val _matchList: MutableLiveData<List<MatchDataModel>> = MutableLiveData()
    val matchList: LiveData<List<MatchDataModel>> get() = _matchList

    private val _recruitList: MutableLiveData<List<TeamItem.RecruitmentItem>> = MutableLiveData()
    val recruitList: LiveData<List<TeamItem.RecruitmentItem>> get() = _recruitList

    private val _applicationList: MutableLiveData<List<TeamItem.ApplicationItem>> = MutableLiveData()
    val applicationList: LiveData<List<TeamItem.ApplicationItem>> get() = _applicationList

    private val _event: MutableLiveData<MyEvent> = MutableLiveData()
    val event: LiveData<MyEvent> get() = _event

    private val database = Firebase.database("https://matching-manager-default-rtdb.asia-southeast1.firebasedatabase.app/")
    private val matchRef = database.getReference("Match")
    private val teamRef = database.getReference("Team")


    fun fetchMatchData(userId : String) {
        viewModelScope.launch {
            val currentList = repository.getMatchList(userId, database)
            Log.d("MatchViewModel", "fetchData() = currentList : ${currentList.size}")

            _matchList.postValue(currentList)
        }
    }

    fun deleteMatch(data: MatchDataModel) {
        viewModelScope.launch {
            repository.deleteMatchData(data, database)
            _event.postValue(MyEvent.Dismiss)
        }
    }

    fun editMatch(data: MatchDataModel, newData : MatchDataModel) {
        viewModelScope.launch {
            repository.editMatchData(data, newData, database)
            _event.postValue(MyEvent.Finish)
        }
    }

    fun autoFetchMatchData() {
        matchRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val dataList = mutableListOf<MatchDataModel>()

                for (childSnapshot in dataSnapshot.children) {
                    val matchData = childSnapshot.getValue(MatchDataModel::class.java)
                    if (matchData != null) {
                        dataList.add(matchData)
                    }
                }
                _matchList.value = dataList
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // 오류 처리
            }
        })
    }

    fun fetchRecruitData(userId : String) {
        viewModelScope.launch {
            val currentList = repository.getRecruitList(userId, database)
            Log.d("MatchViewModel", "fetchData() = currentList : ${currentList.size}")

            _recruitList.postValue(currentList)
        }
    }

    fun deleteRecruit(data: TeamItem.RecruitmentItem) {
        viewModelScope.launch {
            repository.deleteRecruitData(data, database)
            _event.postValue(MyEvent.Dismiss)
        }
    }

    fun editRecruit(data: TeamItem.RecruitmentItem, newData : TeamItem.RecruitmentItem) {
        viewModelScope.launch {
            repository.editRecruitData(data, newData, database)
            _event.postValue(MyEvent.Finish)
        }
    }

    fun autoFetchRecruitData() {
        val query = teamRef.orderByChild("type").equalTo("용병모집")
        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val dataList = mutableListOf<TeamItem.RecruitmentItem>()

                for (childSnapshot in dataSnapshot.children) {
                    val matchData = childSnapshot.getValue(TeamItem.RecruitmentItem::class.java)
                    if (matchData != null) {
                        dataList.add(matchData)
                    }
                }
                _recruitList.value = dataList
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // 오류 처리
            }
        })
    }

    fun fetchApplicationData(userId : String) {
        viewModelScope.launch {
            val currentList = repository.getApplicationList(userId, database)
            Log.d("MatchViewModel", "fetchData() = currentList : ${currentList.size}")

            _applicationList.postValue(currentList)
        }
    }

    fun deleteApplication(data: TeamItem.ApplicationItem) {
        viewModelScope.launch {
            repository.deleteApplicationData(data, database)
            _event.postValue(MyEvent.Dismiss)
        }
    }

    fun editApplication(data: TeamItem.ApplicationItem, newData : TeamItem.ApplicationItem) {
        viewModelScope.launch {
            repository.editApplicationData(data, newData, database)
            _event.postValue(MyEvent.Finish)
        }
    }

    fun autoFetchApplicationData() {
        val query = teamRef.orderByChild("type").equalTo("용병신청")
        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val dataList = mutableListOf<TeamItem.ApplicationItem>()

                for (childSnapshot in dataSnapshot.children) {
                    val matchData = childSnapshot.getValue(TeamItem.ApplicationItem::class.java)
                    if (matchData != null) {
                        dataList.add(matchData)
                    }
                }
                _applicationList.value = dataList
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // 오류 처리
            }
        })
    }

}
sealed interface MyEvent {
    object Dismiss : MyEvent
    object Finish : MyEvent
}

