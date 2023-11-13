package com.link_up.matching_manager.domain.repository

import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.FirebaseDatabase
import com.link_up.matching_manager.ui.match.MatchDataModel

interface MatchRepository {
    fun getList(database : FirebaseDatabase, originalList : MutableList<MatchDataModel>, list : MutableLiveData<List<MatchDataModel>>)

    fun autoGetList(database: FirebaseDatabase, list : MutableLiveData<List<MatchDataModel>>)
    suspend fun addData(data: MatchDataModel, database: FirebaseDatabase)
    suspend fun editViewCount(data : MatchDataModel, database: FirebaseDatabase)
    suspend fun editChatCount(data : MatchDataModel, database: FirebaseDatabase)

}