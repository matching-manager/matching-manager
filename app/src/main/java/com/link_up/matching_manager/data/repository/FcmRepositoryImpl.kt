package com.link_up.matching_manager.data.repository

import com.link_up.matching_manager.data.remote.FcmRemoteDataSource
import com.link_up.matching_manager.domain.repository.FcmRepository
import com.link_up.matching_manager.ui.fcm.send.Payload

class FcmRepositoryImpl(
    private val remoteDataSource: com.link_up.matching_manager.data.remote.FcmRemoteDataSource
) : FcmRepository{
    override suspend fun pushNotification(
        payload : Payload
    ) = remoteDataSource.postNotification(
        payload = payload
    )
}