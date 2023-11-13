package com.link_up.matching_manager.data.repository

import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.link_up.matching_manager.ui.match.MatchDataModel
import com.link_up.matching_manager.domain.repository.MatchRepository
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.tasks.await

class MatchRepositoryImpl() : MatchRepository {

    override fun getList(database : FirebaseDatabase, originalList : MutableList<MatchDataModel>, list : MutableLiveData<List<MatchDataModel>>) {
        val matchRef = database.getReference("Match")
        matchRef.addListenerForSingleValueEvent(object : ValueEventListener {
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
        database: FirebaseDatabase,
        list: MutableLiveData<List<MatchDataModel>>
    ) {
        val matchRef = database.getReference("Match")
        matchRef.addValueEventListener(object : ValueEventListener {
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

    override suspend fun addData(data: MatchDataModel, database: FirebaseDatabase) {
        val matchRef = database.getReference("Match")
        matchRef.push().setValue(data).await()
    }

    override suspend fun editViewCount(data: MatchDataModel, database: FirebaseDatabase) {
        val matchRef = database.getReference("Match")
        val query = matchRef.orderByChild("matchId").equalTo(data.matchId)

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

    override suspend fun editChatCount(data: MatchDataModel, database: FirebaseDatabase) {
        val matchRef = database.getReference("Match")
        val query = matchRef.orderByChild("matchId").equalTo(data.matchId)

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