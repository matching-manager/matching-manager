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


//    override suspend fun getList(): Flow<MyMatchItems> = callbackFlow {
//        val listener = matchRef.addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                if (snapshot.exists()) {
//                    for (matchSnapshot in snapshot.children) {
//                        val getData = matchSnapshot.getValue(MyMatchDataModel::class.java)
//                        getData?.let {
//                            trySend(it) // 각 MatchDataModel을 Flow로 보냅니다.
//                        }
//                    }
//                }
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                close(error.toException()) // Flow를 종료하고 예외를 전달합니다.
//            }
//        })
//        awaitClose {
//            // Flow를 닫을 때 ValueEventListener를 제거합니다.
//            matchRef.removeEventListener(listener)
//        }
//    }

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

    override suspend fun deleteData(data: MyMatchDataModel) {
        matchRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (childSnapshot in dataSnapshot.children) {
                    val matchId = childSnapshot.child("matchId").getValue(Int::class.java)

                    if (matchId == data.matchId) {
                        // 특정 matchId와 일치하는 데이터 삭제
                        childSnapshot.ref.removeValue().addOnSuccessListener {
                            Log.d("delete", "success")
                        }.addOnFailureListener {
                            Log.d("delete", "fail")
                        }
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // 읽기 작업 실패
            }
        })
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

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (childSnapshot in dataSnapshot.children) {
                    // 업데이트할 데이터의 특정 필드 값을 수정
                    childSnapshot.ref.updateChildren(dataToUpdate as Map<String, Any>)
                        .addOnSuccessListener {
                            Log.d("update", "success")
                        }.addOnFailureListener {
                            Log.d("update", "fail")
                        }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // 읽기 작업 실패
            }
        })
    }

//    override suspend fun deleteData(data: MyMatchDataModel) {
//        Log.d("deleteData", "")
//        val snapshot = matchRef.get().await()
//        Log.d("snapshot", "${snapshot}")
//        if (snapshot.exists()) {
//            for (matchSnapshot in snapshot.children) {
//                val matchId = matchSnapshot.child("matchId").getValue(Int::class.java)
//
//                if (matchId == data.matchId) {
//                    matchSnapshot.ref.removeValue()
//                }
//            }
//        }
//    }
}