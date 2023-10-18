package com.example.matching_manager.ui.match

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class MatchRepositoryImpl() : MatchRepository {

    val database =
        Firebase.database("https://matching-manager-default-rtdb.asia-southeast1.firebasedatabase.app/")
    val matchRef = database.getReference("Match")

    override suspend fun getList(): List<MatchDataModel> {
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

    override suspend fun addData(data: MatchDataModel) {
        matchRef.push().setValue(data).await()
    }
}