package com.example.matching_manager.ui.team.viewmodel

import com.example.matching_manager.ui.team.TeamItem
import com.google.firebase.database.FirebaseDatabase

interface TeamRepository {

    suspend fun getList(database : FirebaseDatabase): List<TeamItem>

    suspend fun addRecruitmentData(data: TeamItem.RecruitmentItem, database: FirebaseDatabase)

    suspend fun addApplicationData(data: TeamItem.ApplicationItem, database: FirebaseDatabase)
}