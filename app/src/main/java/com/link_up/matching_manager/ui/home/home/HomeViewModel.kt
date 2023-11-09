package com.link_up.matching_manager.ui.home.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.link_up.matching_manager.ui.match.MatchDataModel

class HomeViewModel : ViewModel() {
    // 공지사항 리스트
    private val _announcementList : MutableLiveData<List<AnnouncementDataModel>> = MutableLiveData()
    val announcementList : LiveData<List<AnnouncementDataModel>> get() = _announcementList

    private val _announceModel : MutableLiveData<AnnouncementDataModel> = MutableLiveData()
    val announceModel : LiveData<AnnouncementDataModel> get() = _announceModel

    // 추천 경기매칭 리스트
    private val _matchList : MutableLiveData<List<MatchDataModel>> = MutableLiveData()
    val matchList : LiveData<List<MatchDataModel>> get() = _matchList

    private val database =
        Firebase.database("https://matching-manager-default-rtdb.asia-southeast1.firebasedatabase.app/")
    private val matchRef = database.getReference("Match")
    private val announceRef = database.getReference("Announcement")

    fun announcementData(item : AnnouncementDataModel) {
        _announceModel.value = item
    }


    fun fetchMatchData() {
        matchRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val dataList = mutableListOf<MatchDataModel>()

                for (childSnapshot in dataSnapshot.children) {
                    val matchData = childSnapshot.getValue(MatchDataModel::class.java)
                    if (matchData != null) {
                        dataList.add(matchData)
                    }
                }
                val currentList = dataList
                _matchList.value = currentList
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // 오류 처리
            }
        })
    }

    fun fetchAnnounceData() {
        announceRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val dataList = mutableListOf<AnnouncementDataModel>()

                for (childSnapshot in dataSnapshot.children) {
                    val announceData = childSnapshot.getValue(AnnouncementDataModel::class.java)
                    if (announceData != null) {
                        dataList.add(announceData)
                    }
                }
                val currentList = dataList
                _announcementList.value = currentList
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // 오류 처리
            }
        })
    }

}