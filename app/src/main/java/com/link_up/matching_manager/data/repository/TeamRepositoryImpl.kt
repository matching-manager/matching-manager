package com.link_up.matching_manager.data.repository

import com.link_up.matching_manager.ui.team.TeamItem
import com.link_up.matching_manager.domain.repository.TeamRepository
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue
import kotlinx.coroutines.tasks.await

class TeamRepositoryImpl() : TeamRepository {
    override suspend fun getList(database: FirebaseDatabase): List<TeamItem> {
        val teamRef = database.getReference("Team")
        val items = arrayListOf<TeamItem>()
        val snapshot = teamRef.get().await()
        if (snapshot.exists()) {
            for (teamSnapshot in snapshot.children) {
                val type = teamSnapshot.child("type").getValue(String::class.java)
                if (type == "용병모집") {
                    teamSnapshot.getValue(TeamItem.RecruitmentItem::class.java)?.let { teamData ->
                        items.add(teamData)
                    }
                }
                else {
                    teamSnapshot.getValue(TeamItem.ApplicationItem::class.java)?.let { teamData ->
                        items.add(teamData)
                    }
                }

            }
        }

        return items
    }


    override suspend fun addRecruitmentData(data: TeamItem.RecruitmentItem, database: FirebaseDatabase) {
        val matchRef = database.getReference("Team")
        matchRef.push().setValue(data).await()
    }

    override suspend fun addApplicationData(data: TeamItem.ApplicationItem, database: FirebaseDatabase) {
        val matchRef = database.getReference("Team")
        matchRef.push().setValue(data).await()
    }

    override suspend fun editViewCount(data: TeamItem, database: FirebaseDatabase) {
        val matchRef = database.getReference("Team")
        val query = matchRef.orderByChild("teamId").equalTo(data.teamId)

        val updateValue = ServerValue.increment(1)

        val dataToUpdate = hashMapOf(
            "viewCount" to updateValue
        )
        val snapshot = query.get().await()
        if (snapshot.exists()) {
            for (childSnapshot in snapshot.children) {
                childSnapshot.ref.updateChildren(dataToUpdate as Map<String, Any>)
            }
        }
    }

    override suspend fun editChatCount(data: TeamItem, database: FirebaseDatabase) {
        val matchRef = database.getReference("Team")
        val query = matchRef.orderByChild("teamId").equalTo(data.teamId)

        val updateValue = ServerValue.increment(1)

        val dataToUpdate = hashMapOf(
            "chatCount" to updateValue
        )
        val snapshot = query.get().await()
        if (snapshot.exists()) {
            for (childSnapshot in snapshot.children) {
                childSnapshot.ref.updateChildren(dataToUpdate as Map<String, Any>)
            }
        }    }


}