package com.example.matching_manager.ui.my

interface MyMatchRepository {
    suspend fun getList(): List<MyMatchDataModel>
}