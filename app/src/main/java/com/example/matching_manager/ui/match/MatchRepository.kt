package com.example.matching_manager.ui.match

interface MatchRepository {
    suspend fun getList(): List<MatchDataModel>
    suspend fun addData(data: MatchDataModel)
}