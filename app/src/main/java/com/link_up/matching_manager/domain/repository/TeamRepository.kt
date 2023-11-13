package com.link_up.matching_manager.domain.repository

import androidx.lifecycle.MutableLiveData
import com.link_up.matching_manager.ui.team.TeamItem
import com.google.firebase.database.FirebaseDatabase

interface TeamRepository {
    fun autoGetList(database: FirebaseDatabase, list : MutableLiveData<List<TeamItem>>)

    suspend fun addRecruitmentData(data: TeamItem.RecruitmentItem, database: FirebaseDatabase)

    suspend fun addApplicationData(data: TeamItem.ApplicationItem, database: FirebaseDatabase)

    suspend fun editViewCount(data: TeamItem, database: FirebaseDatabase)

    suspend fun editChatCount(data: TeamItem, database: FirebaseDatabase)
}