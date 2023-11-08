package com.example.matching_manager.domain.repository

import com.example.matching_manager.ui.team.TeamItem
import com.google.firebase.database.FirebaseDatabase

interface TeamRepository {

    suspend fun getList(database : FirebaseDatabase): List<TeamItem>

    suspend fun addRecruitmentData(data: TeamItem.RecruitmentItem, database: FirebaseDatabase)

    suspend fun addApplicationData(data: TeamItem.ApplicationItem, database: FirebaseDatabase)

    suspend fun editViewCount(data: TeamItem, database: FirebaseDatabase)

    suspend fun editChatCount(data: TeamItem, database: FirebaseDatabase)
}