package com.example.matching_manager.data.repository

import com.example.matching_manager.data.remote.FcmRemoteDataSource
import com.example.matching_manager.domain.repository.FcmRepository
import com.example.matching_manager.fcm.Payload
import com.example.matching_manager.fcm.PayloadNotification

class FcmRepositoryImpl(
    private val remoteDataSource: FcmRemoteDataSource
) : FcmRepository{
    override suspend fun pushNotification(
        payload : String
    ) = remoteDataSource.postNotification(
        payload = payload
    )
}