package com.example.matching_manager.domain.repository.database

import com.example.matching_manager.data.model.UserInfoModel
import com.google.firebase.database.FirebaseDatabase

interface UserInfoRepository {

    suspend fun adduser(data: UserInfoModel, database: FirebaseDatabase)
    suspend fun checkUser(data: UserInfoModel,database: FirebaseDatabase) : Boolean

}