package com.link_up.matching_manager.domain.repository.database

import com.link_up.matching_manager.data.model.UserInfoModel
import com.google.firebase.database.FirebaseDatabase

interface UserInfoRepository {

    suspend fun adduser(data: com.link_up.matching_manager.data.model.UserInfoModel, database: FirebaseDatabase)
    suspend fun checkUser(data: com.link_up.matching_manager.data.model.UserInfoModel, database: FirebaseDatabase) : Boolean

}