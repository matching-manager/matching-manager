package com.example.matching_manager.ui.match

interface MatchRepository2 {
    suspend fun getList(): List<MatchDataModel>
    fun addData(data: MatchDataModel)
}