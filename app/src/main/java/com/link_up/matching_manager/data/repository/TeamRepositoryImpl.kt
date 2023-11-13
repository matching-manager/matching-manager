package com.link_up.matching_manager.data.repository

import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.link_up.matching_manager.ui.team.TeamItem
import com.link_up.matching_manager.domain.repository.TeamRepository
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue
import com.google.firebase.database.ValueEventListener
import com.link_up.matching_manager.util.UserInformation
import kotlinx.coroutines.tasks.await

class TeamRepositoryImpl() : TeamRepository {
    override fun autoGetList(databaseRef : DatabaseReference, list: MutableLiveData<List<TeamItem>>) {
        databaseRef.addValueEventListener(object : ValueEventListener {
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

    override fun autoGetRecruitList(
        databaseRef: DatabaseReference,
        list: MutableLiveData<List<TeamItem.RecruitmentItem>>
    ) {
        val query = databaseRef.orderByChild("type").equalTo("용병모집")
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

    override fun autoGetApplicationList(
        databaseRef: DatabaseReference,
        list: MutableLiveData<List<TeamItem.ApplicationItem>>
    ) {
        val query = databaseRef.orderByChild("type").equalTo("용병신청")
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


    override suspend fun addRecruitmentData(databaseRef : DatabaseReference, data: TeamItem.RecruitmentItem) {
        databaseRef.push().setValue(data).await()
    }

    override suspend fun addApplicationData(databaseRef : DatabaseReference, data: TeamItem.ApplicationItem) {
        databaseRef.push().setValue(data).await()
    }

    override suspend fun deleteRecruitData(
        databaseRef: DatabaseReference,
        data: TeamItem.RecruitmentItem
    ) {
        val query = databaseRef.orderByChild("teamId").equalTo(data.teamId)
        val snapshot = query.get().await()
        if (snapshot.exists()) {
            for (childSnapshot in snapshot.children) {
                childSnapshot.ref.removeValue()
            }
        }
    }

    override suspend fun deleteApplicationData(
        databaseRef: DatabaseReference,
        data: TeamItem.ApplicationItem
    ) {
        val query = databaseRef.orderByChild("teamId").equalTo(data.teamId)
        val snapshot = query.get().await()
        if (snapshot.exists()) {
            for (childSnapshot in snapshot.children) {
                childSnapshot.ref.removeValue()
            }
        }
    }

    override suspend fun editRecruitData(
        databaseRef: DatabaseReference,
        data: TeamItem.RecruitmentItem,
        newData: TeamItem.RecruitmentItem
    ) {
        val query = databaseRef.orderByChild("teamId").equalTo(data.teamId)

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

    override suspend fun editApplicationData(
        databaseRef: DatabaseReference,
        data: TeamItem.ApplicationItem,
        newData: TeamItem.ApplicationItem
    ) {
        val query = databaseRef.orderByChild("teamId").equalTo(data.teamId)

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

    override suspend fun editViewCount(databaseRef : DatabaseReference, data: TeamItem) {
        val query = databaseRef.orderByChild("teamId").equalTo(data.teamId)

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

    override suspend fun editChatCount(databaseRef : DatabaseReference, data: TeamItem) {
        val query = databaseRef.orderByChild("teamId").equalTo(data.teamId)

        val updateValue = ServerValue.increment(1)

        val dataToUpdate = hashMapOf(
            "chatCount" to updateValue
        )
        val snapshot = query.get().await()
        if (snapshot.exists()) {
            for (childSnapshot in snapshot.children) {
                childSnapshot.ref.updateChildren(dataToUpdate as Map<String, Any>)
            }
        }
    }
}