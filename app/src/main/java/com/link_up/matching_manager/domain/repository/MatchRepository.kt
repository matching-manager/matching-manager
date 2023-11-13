package com.link_up.matching_manager.domain.repository

import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.link_up.matching_manager.ui.match.MatchDataModel

interface MatchRepository {
    fun getList(databaseRef : DatabaseReference, originalList : MutableList<MatchDataModel>, list : MutableLiveData<List<MatchDataModel>>)

    fun autoGetList(databaseRef : DatabaseReference, list : MutableLiveData<List<MatchDataModel>>)
    suspend fun addData(databaseRef : DatabaseReference, data: MatchDataModel)
    suspend fun editViewCount(databaseRef : DatabaseReference, data : MatchDataModel)
    suspend fun editChatCount(databaseRef : DatabaseReference, data : MatchDataModel)

}