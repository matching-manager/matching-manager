package com.example.matching_manager.ui.my

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class MyMatchRepositoryImpl : MyMatchRepository{

    val database =
        Firebase.database("https://matching-manager-default-rtdb.asia-southeast1.firebasedatabase.app/")
    val matchRef = database.getReference("Match")

    override suspend fun getList(): List<MyMatchDataModel> {
        val items = arrayListOf<MyMatchDataModel>()
        val snapshot = matchRef.get().await()
        if (snapshot.exists()) {
            for (matchSnapshot in snapshot.children) {
                matchSnapshot.getValue(MyMatchDataModel::class.java)?.let { matchData ->
                    items.add(matchData)
                }
            }
        }

        return items
    }
}