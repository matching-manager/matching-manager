package com.example.matching_manager.ui.match

interface MatchRepository {
    suspend fun getList(): MutableList<MatchDataModel>
}