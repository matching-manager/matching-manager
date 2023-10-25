package com.example.matching_manager.ui.my

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
class MyViewModel(private val repository: MyMatchRepository) : ViewModel() {

    private val _list: MutableLiveData<List<MyMatchDataModel>> = MutableLiveData()
    val list: LiveData<List<MyMatchDataModel>> get() = _list

    val userId = "userId"

    private val _event: MutableLiveData<MatchEvent> = MutableLiveData()
    val event: LiveData<MatchEvent> get() = _event

    val database = Firebase.database("https://matching-manager-default-rtdb.asia-southeast1.firebasedatabase.app/")
    val matchRef = database.getReference("Match")



    fun fetchData(userId : String) {
        viewModelScope.launch {
            val currentList = repository.getList(userId)
            Log.d("MatchViewModel", "fetchData() = currentList : ${currentList.size}")

            _list.postValue(currentList)
        }
    }

    fun deleteMatch(data: MyMatchDataModel) {
        viewModelScope.launch {
            repository.deleteData(data)
            _event.postValue(MatchEvent.Dismiss)
        }
    }

    fun editMatch(data: MyMatchDataModel, newData : MyMatchDataModel) {
        viewModelScope.launch {
            repository.editData(data, newData)
            _event.postValue(MatchEvent.Finish)
        }
    }

    fun autoFetchData() {
        matchRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val dataList = mutableListOf<MyMatchDataModel>()

                for (childSnapshot in dataSnapshot.children) {
                    val matchData = childSnapshot.getValue(MyMatchDataModel::class.java)
                    if (matchData != null) {
                        dataList.add(matchData)
                    }
                }
                _list.value = dataList
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // 오류 처리
            }
        })
    }

}
sealed interface MatchEvent {
    object Dismiss : MatchEvent
    object Finish : MatchEvent
}

