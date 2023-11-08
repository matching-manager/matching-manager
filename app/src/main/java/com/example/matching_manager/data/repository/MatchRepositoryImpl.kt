package com.example.matching_manager.data.repository

import com.example.matching_manager.ui.match.MatchDataModel
import com.example.matching_manager.domain.repository.MatchRepository
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue
import kotlinx.coroutines.tasks.await

class MatchRepositoryImpl() : MatchRepository {

    override suspend fun getList(database : FirebaseDatabase): List<MatchDataModel> {
        val matchRef = database.getReference("Match")
        val items = arrayListOf<MatchDataModel>()
        val snapshot = matchRef.get().await()
        if (snapshot.exists()) {
            for (matchSnapshot in snapshot.children) {
                 matchSnapshot.getValue(MatchDataModel::class.java)?.let { matchData ->
                     items.add(matchData)
                }
            }
        }
        return items
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