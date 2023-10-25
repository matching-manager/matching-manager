package com.example.matching_manager.ui.my

import com.google.firebase.database.FirebaseDatabase

interface MyMatchRepository {
    suspend fun getList(userId : String, database: FirebaseDatabase): List<MyMatchDataModel>

    suspend fun deleteData(data : MyMatchDataModel, database: FirebaseDatabase)

    suspend fun editData(data : MyMatchDataModel, newData: MyMatchDataModel, database: FirebaseDatabase)
}