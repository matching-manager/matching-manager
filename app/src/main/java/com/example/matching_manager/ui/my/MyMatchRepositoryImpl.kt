package com.example.matching_manager.ui.my

import android.util.Log
import android.widget.Toast
import com.example.matching_manager.ui.match.MatchDataModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.gson.GsonBuilder
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.util.concurrent.Flow

class MyMatchRepositoryImpl() : MyMatchRepository {

    val database =
        Firebase.database("https://matching-manager-default-rtdb.asia-southeast1.firebasedatabase.app/")
    val matchRef = database.getReference("Match")

    override suspend fun getList(userId : String): List<MyMatchDataModel> {
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

    override suspend fun deleteData(data: MyMatchDataModel) {
        Log.d("deleteData", "start")
        val query = matchRef.orderByChild("matchId").equalTo(data.matchId.toDouble())
        try {
            val snapshot = query.get().await()
            Log.d("deleteData", "${snapshot}")
            if (snapshot.exists()) {
                for (childSnapshot in snapshot.children) {
                    childSnapshot.ref.removeValue()
                }
            }
        } catch (e: Exception) {
            Log.e("deleteData", "Error deleting data: $e")
        }
    }

    override suspend fun editData(data: MyMatchDataModel, newData: MyMatchDataModel) {
        val query = matchRef.orderByChild("matchId").equalTo(data.matchId.toDouble())

        val dataToUpdate = hashMapOf(
            "teamName" to newData.teamName,
            "game" to newData.game,
            "schedule" to newData.schedule,
            "matchPlace" to newData.matchPlace,
            "playerNum" to newData.playerNum,
            "entryFee" to newData.entryFee,
            "description" to newData.description,
            "gender" to newData.gender,
        )
        val snapshot = query.get().await()
        if (snapshot.exists()) {
            for (childSnapshot in snapshot.children) {
                childSnapshot.ref.updateChildren(dataToUpdate as Map<String, Any>)
            }
        }
    }
}