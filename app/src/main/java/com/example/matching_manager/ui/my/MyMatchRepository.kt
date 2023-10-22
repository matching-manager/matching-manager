package com.example.matching_manager.ui.my

interface MyMatchRepository {
    suspend fun getList(userId : String): List<MyMatchDataModel>

    suspend fun deleteData(data : MyMatchDataModel)

    suspend fun editData(data : MyMatchDataModel, newData: MyMatchDataModel)
}