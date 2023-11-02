package com.example.matching_manager.domain.usecase.database

import com.example.matching_manager.data.model.UserInfoModel
import com.example.matching_manager.domain.repository.database.UserInfoRepository
import com.google.firebase.database.FirebaseDatabase

class AddUserInfoToDataBaseUseCase(
    private val repository: UserInfoRepository
) {
    suspend operator fun invoke(
        data: UserInfoModel,
        database: FirebaseDatabase
    ) = repository.adduser(
        data= data,
        database= database
    )
}