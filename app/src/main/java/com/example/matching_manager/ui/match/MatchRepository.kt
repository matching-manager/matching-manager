package com.example.matching_manager.ui.match

interface MatchRepository {
    fun getList(): MutableList<MatchDataModel>
}