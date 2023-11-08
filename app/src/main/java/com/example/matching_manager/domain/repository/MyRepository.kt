package com.example.matching_manager.domain.repository

import com.example.matching_manager.ui.match.MatchDataModel
import com.example.matching_manager.ui.team.TeamItem
import com.google.firebase.database.FirebaseDatabase

interface MyRepository {
    suspend fun getMatchList(userId : String, database: FirebaseDatabase): List<MatchDataModel>

    suspend fun deleteMatchData(data : MatchDataModel, database: FirebaseDatabase)

    suspend fun editMatchData(data : MatchDataModel, newData: MatchDataModel, database: FirebaseDatabase)

    suspend fun getRecruitList(userId : String, database: FirebaseDatabase): List<TeamItem.RecruitmentItem>

    suspend fun deleteRecruitData(data : TeamItem.RecruitmentItem, database: FirebaseDatabase)

    suspend fun editRecruitData(data : TeamItem.RecruitmentItem, newData: TeamItem.RecruitmentItem, database: FirebaseDatabase)

    suspend fun getApplicationList(userId : String, database: FirebaseDatabase): List<TeamItem.ApplicationItem>

    suspend fun deleteApplicationData(data : TeamItem.ApplicationItem, database: FirebaseDatabase)

    suspend fun editApplicationData(data : TeamItem.ApplicationItem, newData: TeamItem.ApplicationItem, database: FirebaseDatabase)
}