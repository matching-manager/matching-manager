package com.link_up.matching_manager.domain.repository

import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DatabaseReference
import com.link_up.matching_manager.ui.team.TeamItem
import com.google.firebase.database.FirebaseDatabase

interface TeamRepository {
    fun autoGetList(databaseRef : DatabaseReference, list : MutableLiveData<List<TeamItem>>)

    suspend fun addRecruitmentData(databaseRef : DatabaseReference, data: TeamItem.RecruitmentItem)

    suspend fun addApplicationData(databaseRef : DatabaseReference, data: TeamItem.ApplicationItem)

    suspend fun editViewCount(databaseRef : DatabaseReference, data: TeamItem)

    suspend fun editChatCount(databaseRef : DatabaseReference, data: TeamItem)
}