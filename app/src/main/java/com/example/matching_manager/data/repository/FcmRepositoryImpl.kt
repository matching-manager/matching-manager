package com.example.matching_manager.data.repository

import com.example.matching_manager.data.remote.FcmRemoteDataSource
import com.example.matching_manager.domain.repository.FcmRepository
import com.example.matching_manager.ui.fcm.send.Payload

class FcmRepositoryImpl(
    private val remoteDataSource: FcmRemoteDataSource
) : FcmRepository{
    override suspend fun pushNotification(
        payload : Payload
    ) = remoteDataSource.postNotification(
        payload = payload
    )
}