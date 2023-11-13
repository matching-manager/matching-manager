package com.link_up.matching_manager.domain.repository

import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DatabaseReference
import com.link_up.matching_manager.ui.team.TeamItem
import com.google.firebase.database.FirebaseDatabase

interface TeamRepository {
    fun autoGetList(databaseRef : DatabaseReference, list : MutableLiveData<List<TeamItem>>)

    fun autoGetRecruitList(databaseRef : DatabaseReference, list : MutableLiveData<List<TeamItem.RecruitmentItem>>)

    fun autoGetApplicationList(databaseRef : DatabaseReference, list : MutableLiveData<List<TeamItem.ApplicationItem>>)

    suspend fun addRecruitmentData(databaseRef : DatabaseReference, data: TeamItem.RecruitmentItem)

    suspend fun addApplicationData(databaseRef : DatabaseReference, data: TeamItem.ApplicationItem)

    suspend fun deleteRecruitData(databaseRef : DatabaseReference, data: TeamItem.RecruitmentItem)

    suspend fun deleteApplicationData(databaseRef : DatabaseReference, data: TeamItem.ApplicationItem)

    suspend fun editTeamData(databaseRef : DatabaseReference, data : TeamItem, newData: TeamItem)

    suspend fun editViewCount(databaseRef : DatabaseReference, data: TeamItem)

    suspend fun editChatCount(databaseRef : DatabaseReference, data: TeamItem)
}