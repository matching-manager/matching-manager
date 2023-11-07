package com.example.matching_manager.ui.match

import com.google.firebase.database.FirebaseDatabase

interface MatchRepository {
    suspend fun getList(database : FirebaseDatabase): List<MatchDataModel>
    suspend fun addData(data: MatchDataModel, database: FirebaseDatabase)

    suspend fun editViewCount(data : MatchDataModel, database: FirebaseDatabase)

    suspend fun editChatCount(data : MatchDataModel, database: FirebaseDatabase)

}