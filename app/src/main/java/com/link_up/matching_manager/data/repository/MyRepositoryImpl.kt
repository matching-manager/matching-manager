package com.link_up.matching_manager.data.repository

import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.link_up.matching_manager.ui.match.MatchDataModel
import com.link_up.matching_manager.domain.repository.MyRepository
import com.link_up.matching_manager.ui.team.TeamItem
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.link_up.matching_manager.util.UserInformation
import kotlinx.coroutines.tasks.await

class MyRepositoryImpl() : MyRepository {


    override suspend fun editRecruitData(data: TeamItem.RecruitmentItem, newData: TeamItem.RecruitmentItem, database: FirebaseDatabase) {
        val teamRef = database.getReference("Team")
        val query = teamRef.orderByChild("teamId").equalTo(data.teamId)

        val dataToUpdate = hashMapOf(
            "teamName" to newData.teamName,
            "game" to newData.game,
            "schedule" to newData.schedule,
            "playerNum" to newData.playerNum,
            "area" to newData.area,
            "gender" to newData.gender,
            "level" to newData.level,
            "pay" to newData.pay,
            "description" to newData.description,
            "postImg" to newData.postImg
        )
        val snapshot = query.get().await()
        if (snapshot.exists()) {
            for (childSnapshot in snapshot.children) {
                childSnapshot.ref.updateChildren(dataToUpdate as Map<String, Any>)
            }
        }
    }

    override suspend fun editApplicationData(data: TeamItem.ApplicationItem, newData: TeamItem.ApplicationItem, database: FirebaseDatabase) {
        val teamRef = database.getReference("Team")
        val query = teamRef.orderByChild("teamId").equalTo(data.teamId)

        val dataToUpdate = hashMapOf(
            "game" to newData.game,
            "schedule" to newData.schedule,
            "playerNum" to newData.playerNum,
            "area" to newData.area,
            "gender" to newData.gender,
            "level" to newData.level,
            "description" to newData.description,
            "postImg" to newData.postImg,
            "age" to newData.age,
        )
        val snapshot = query.get().await()
        if (snapshot.exists()) {
            for (childSnapshot in snapshot.children) {
                childSnapshot.ref.updateChildren(dataToUpdate as Map<String, Any>)
            }
        }
    }
}