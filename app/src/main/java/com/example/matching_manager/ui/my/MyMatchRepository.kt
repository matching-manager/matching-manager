package com.example.matching_manager.ui.my

import com.example.matching_manager.ui.team.TeamItem
import com.google.firebase.database.FirebaseDatabase

interface MyMatchRepository {
    suspend fun getMatchList(userId : String, database: FirebaseDatabase): List<MyMatchDataModel>

    suspend fun deleteMatchData(data : MyMatchDataModel, database: FirebaseDatabase)

    suspend fun editMatchData(data : MyMatchDataModel, newData: MyMatchDataModel, database: FirebaseDatabase)

    suspend fun getRecruitList(userId : String, database: FirebaseDatabase): List<TeamItem.RecruitmentItem>

    suspend fun deleteRecruitData(data : TeamItem.RecruitmentItem, database: FirebaseDatabase)

    suspend fun editRecruitData(data : TeamItem.RecruitmentItem, newData: TeamItem.RecruitmentItem, database: FirebaseDatabase)

    suspend fun getApplicationList(userId : String, database: FirebaseDatabase): List<TeamItem.ApplicationItem>

    suspend fun deleteApplicationData(data : TeamItem.ApplicationItem, database: FirebaseDatabase)

    suspend fun editApplicationData(data : TeamItem.ApplicationItem, newData: TeamItem.ApplicationItem, database: FirebaseDatabase)
}