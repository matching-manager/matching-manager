package com.link_up.matching_manager.domain.usecase.database

import com.link_up.matching_manager.data.model.UserInfoModel
import com.link_up.matching_manager.domain.repository.database.UserInfoRepository
import com.google.firebase.database.FirebaseDatabase

class CheckUserInfoToDataBaseUseCase(
    private val repository: UserInfoRepository
) {
    suspend operator fun invoke(
        data: com.link_up.matching_manager.data.model.UserInfoModel,
        database: FirebaseDatabase
    ): Boolean = repository.checkUser(
        data = data,
        database = database
    )
}