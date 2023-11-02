package com.example.matching_manager.ui.team.viewmodel

import com.example.matching_manager.ui.team.TeamItem
import com.google.firebase.database.FirebaseDatabase
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


}