package com.example.matching_manager.domain.usecase.database

import com.example.matching_manager.data.model.UserInfoModel
import com.example.matching_manager.domain.repository.database.UserInfoRepository
import com.google.firebase.database.FirebaseDatabase

class CheckUserInfoToDataBaseUseCase(
    private val repository: UserInfoRepository
) {
    suspend operator fun invoke(
        data: UserInfoModel,
        database: FirebaseDatabase
    ): Boolean = repository.checkUser(
        data = data,
        database = database
    )
}