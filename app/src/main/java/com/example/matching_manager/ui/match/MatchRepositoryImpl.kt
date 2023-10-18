package com.example.matching_manager.ui.match

import android.service.autofill.FieldClassification.Match
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.matching_manager.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MatchRepositoryImpl()  : MatchRepository{

    val database = Firebase.database("https://matching-manager-default-rtdb.asia-southeast1.firebasedatabase.app/")
    val matchRef = database.getReference("Match")


    override fun getList(): LiveData<MutableList<MatchDataModel>> {
            val mutableData = MutableLiveData<MutableList<MatchDataModel>>()
        matchRef.addValueEventListener(object : ValueEventListener {
            val listData: MutableList<MatchDataModel> = mutableListOf<MatchDataModel>()
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    listData.clear()
                    for (matchSnapshot in snapshot.children){
                        val getData = matchSnapshot.getValue(MatchDataModel::class.java)
                        listData.add(getData!!)
                        mutableData.value = listData
                    }

                    Log.d("mutable", "getList() = listData : ${listData.size}")
                }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
        return mutableData
    }

    override fun addData(data: MatchDataModel) {
        matchRef.push().setValue(data)
    }
}