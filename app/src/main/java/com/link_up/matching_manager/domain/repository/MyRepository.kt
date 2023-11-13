package com.link_up.matching_manager.domain.repository

import androidx.lifecycle.MutableLiveData
import com.link_up.matching_manager.ui.match.MatchDataModel
import com.link_up.matching_manager.ui.team.TeamItem
import com.google.firebase.database.FirebaseDatabase

interface MyRepository {
    fun getMatchList(database: FirebaseDatabase, list : MutableLiveData<List<MatchDataModel>>)

    suspend fun deleteMatchData(data : MatchDataModel, database: FirebaseDatabase)

    suspend fun editMatchData(data : MatchDataModel, newData: MatchDataModel, database: FirebaseDatabase)

    fun getRecruitList(database: FirebaseDatabase, list : MutableLiveData<List<TeamItem.RecruitmentItem>>)

    suspend fun deleteRecruitData(data : TeamItem.RecruitmentItem, database: FirebaseDatabase)

    suspend fun editRecruitData(data : TeamItem.RecruitmentItem, newData: TeamItem.RecruitmentItem, database: FirebaseDatabase)

    fun getApplicationList(database: FirebaseDatabase, list : MutableLiveData<List<TeamItem.ApplicationItem>>)

    suspend fun deleteApplicationData(data : TeamItem.ApplicationItem, database: FirebaseDatabase)

    suspend fun editApplicationData(data : TeamItem.ApplicationItem, newData: TeamItem.ApplicationItem, database: FirebaseDatabase)
}