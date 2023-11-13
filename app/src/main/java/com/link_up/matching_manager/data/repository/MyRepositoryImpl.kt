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
    override fun getRecruitList(database: FirebaseDatabase, list: MutableLiveData<List<TeamItem.RecruitmentItem>>) {
        val teamRef = database.getReference("Team")
        val query = teamRef.orderByChild("type").equalTo("용병모집")
        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val dataList = mutableListOf<TeamItem.RecruitmentItem>()

                for (childSnapshot in dataSnapshot.children) {
                    val recruitData = childSnapshot.getValue(TeamItem.RecruitmentItem::class.java)
                    if (recruitData != null && recruitData.userId == UserInformation.userInfo.uid) {
                        dataList.add(recruitData)
                    }
                }
                list.value = dataList
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // 오류 처리
            }
        })
    }

    override suspend fun deleteRecruitData(data: TeamItem.RecruitmentItem, database: FirebaseDatabase) {
        val teamRef = database.getReference("Team")
        val query = teamRef.orderByChild("teamId").equalTo(data.teamId)
        val snapshot = query.get().await()
        if (snapshot.exists()) {
            for (childSnapshot in snapshot.children) {
                childSnapshot.ref.removeValue()
            }
        }
    }

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

    override fun getApplicationList(database: FirebaseDatabase, list: MutableLiveData<List<TeamItem.ApplicationItem>>) {
        val teamRef = database.getReference("Team")
        val query = teamRef.orderByChild("type").equalTo("용병신청")
        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val dataList = mutableListOf<TeamItem.ApplicationItem>()

                for (childSnapshot in dataSnapshot.children) {
                    val applicationData = childSnapshot.getValue(TeamItem.ApplicationItem::class.java)
                    if (applicationData != null && applicationData.userId == UserInformation.userInfo.uid) {
                        dataList.add(applicationData)
                    }
                }
                list.value = dataList
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // 오류 처리
            }
        })
    }

    override suspend fun deleteApplicationData(data: TeamItem.ApplicationItem, database: FirebaseDatabase) {
        val teamRef = database.getReference("Team")
        val query = teamRef.orderByChild("teamId").equalTo(data.teamId)
        val snapshot = query.get().await()
        if (snapshot.exists()) {
            for (childSnapshot in snapshot.children) {
                childSnapshot.ref.removeValue()
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