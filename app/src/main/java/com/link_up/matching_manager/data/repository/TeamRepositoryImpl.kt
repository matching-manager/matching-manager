package com.link_up.matching_manager.data.repository

import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.link_up.matching_manager.ui.team.TeamItem
import com.link_up.matching_manager.domain.repository.TeamRepository
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.tasks.await

class TeamRepositoryImpl() : TeamRepository {
    override fun autoGetList(database: FirebaseDatabase, list: MutableLiveData<List<TeamItem>>) {
        val teamRef = database.getReference("Team")
        teamRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val dataList = mutableListOf<TeamItem>()

                for (childSnapshot in dataSnapshot.children) {
                    val type = childSnapshot.child("type").getValue(String::class.java)
                    if (type == "용병모집") {
                        childSnapshot.getValue(TeamItem.RecruitmentItem::class.java)
                            ?.let { teamData ->
                                dataList.add(teamData)
                            }
                    } else {
                        childSnapshot.getValue(TeamItem.ApplicationItem::class.java)
                            ?.let { teamData ->
                                dataList.add(teamData)
                            }
                    }
                }
                list.value = dataList
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // 오류 처리
            }
        })
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