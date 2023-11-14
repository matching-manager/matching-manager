package com.link_up.matching_manager.data.repository

import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.link_up.matching_manager.ui.match.MatchDataModel
import com.link_up.matching_manager.domain.repository.MatchRepository
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.database.ServerValue
import com.google.firebase.database.ValueEventListener
import com.link_up.matching_manager.ui.team.TeamItem
import kotlinx.coroutines.tasks.await

class MatchRepositoryImpl() : MatchRepository {

    override fun getList(databaseRef : DatabaseReference, originalList : MutableList<MatchDataModel>, list : MutableLiveData<List<MatchDataModel>>) {
        databaseRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val dataList = mutableListOf<MatchDataModel>()

                for (childSnapshot in dataSnapshot.children) {
                    val matchData = childSnapshot.getValue(MatchDataModel::class.java)
                    if (matchData != null) {
                        dataList.add(matchData)
                    }
                }
                val currentList = dataList
                originalList.clear()
                originalList.addAll(dataList)
                list.value = currentList
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // 오류 처리
            }
        })
    }

    override fun autoGetList(
        databaseRef : DatabaseReference?,
        query : Query?,
        list: MutableLiveData<List<MatchDataModel>>
    ) {
        val actualQuery = query ?: databaseRef
        actualQuery?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val dataList = mutableListOf<MatchDataModel>()

                for (childSnapshot in dataSnapshot.children) {
                    val matchData = childSnapshot.getValue(MatchDataModel::class.java)
                    if (matchData != null) {
                        dataList.add(matchData)
                    }
                }
                list.value = dataList
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // 오류 처리
            }
        })
    }

    override suspend fun addData(databaseRef : DatabaseReference, data: MatchDataModel) {
        databaseRef.push().setValue(data).await()
    }

    override suspend fun deleteData(databaseRef: DatabaseReference, data: MatchDataModel) {
        val query = databaseRef.orderByChild("matchId").equalTo(data.matchId)
        val snapshot = query.get().await()
        if (snapshot.exists()) {
            for (childSnapshot in snapshot.children) {
                childSnapshot.ref.removeValue()
            }
        }
    }

    override suspend fun editMatchData(
        databaseRef: DatabaseReference,
        data: MatchDataModel,
        newData: MatchDataModel
    ) {
        val query = databaseRef.orderByChild("matchId").equalTo(data.matchId)

        val dataToUpdate = hashMapOf(
            "teamName" to newData.teamName,
            "game" to newData.game,
            "schedule" to newData.schedule,
            "playerNum" to newData.playerNum,
            "matchPlace" to newData.matchPlace,
            "gender" to newData.gender,
            "level" to newData.level,
            "entryFee" to newData.entryFee,
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

    override suspend fun editViewCount(query: Query, data: MatchDataModel) {
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

    override suspend fun editChatCount(query: Query,data: MatchDataModel) {
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