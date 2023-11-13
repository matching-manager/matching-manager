package com.link_up.matching_manager.domain.repository

import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.link_up.matching_manager.ui.match.MatchDataModel

interface MatchRepository {
    fun getList(databaseRef : DatabaseReference, originalList : MutableList<MatchDataModel>, list : MutableLiveData<List<MatchDataModel>>)

    fun autoGetList(databaseRef : DatabaseReference? = null, query: Query? = null,  list : MutableLiveData<List<MatchDataModel>>)
    suspend fun addData(databaseRef : DatabaseReference, data: MatchDataModel)

    suspend fun deleteData(databaseRef : DatabaseReference, data : MatchDataModel)
    suspend fun editMatchData(databaseRef: DatabaseReference, data : MatchDataModel, newData: MatchDataModel)
    suspend fun editViewCount(databaseRef : DatabaseReference, data : MatchDataModel)
    suspend fun editChatCount(databaseRef : DatabaseReference, data : MatchDataModel)

}