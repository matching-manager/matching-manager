package com.example.matching_manager.ui.match

import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
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
}