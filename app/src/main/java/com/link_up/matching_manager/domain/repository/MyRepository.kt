package com.link_up.matching_manager.domain.repository

import androidx.lifecycle.MutableLiveData
import com.link_up.matching_manager.ui.match.MatchDataModel
import com.link_up.matching_manager.ui.team.TeamItem
import com.google.firebase.database.FirebaseDatabase

interface MyRepository {

    suspend fun editRecruitData(data : TeamItem.RecruitmentItem, newData: TeamItem.RecruitmentItem, database: FirebaseDatabase)

    suspend fun editApplicationData(data : TeamItem.ApplicationItem, newData: TeamItem.ApplicationItem, database: FirebaseDatabase)
}