package com.example.matching_manager.ui.my

import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await

class MyMatchRepositoryImpl() : MyMatchRepository {

    override suspend fun getList(userId : String, database: FirebaseDatabase): List<MyMatchDataModel> {
        val matchRef = database.getReference("Match")
        val items = arrayListOf<MyMatchDataModel>()
        val query = matchRef.orderByChild("userId").equalTo(userId)
        val snapshot = query.get().await()
        if (snapshot.exists()) {
            for (childSnapshot in snapshot.children) {
                childSnapshot.getValue(MyMatchDataModel::class.java)?.let { matchData ->
                    items.add(matchData)
                }
            }
        }
        return items
    }

    override suspend fun deleteData(data: MyMatchDataModel, database: FirebaseDatabase) {
        val matchRef = database.getReference("Match")
        val query = matchRef.orderByChild("matchId").equalTo(data.matchId)
        val snapshot = query.get().await()
        if (snapshot.exists()) {
            for (childSnapshot in snapshot.children) {
                childSnapshot.ref.removeValue()
            }
        }
    }

    override suspend fun editData(data: MyMatchDataModel, newData: MyMatchDataModel, database: FirebaseDatabase) {
        val matchRef = database.getReference("Match")
        val query = matchRef.orderByChild("matchId").equalTo(data.matchId)

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
}