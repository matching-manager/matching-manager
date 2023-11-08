package com.link_up.matching_manager.data.repository

import com.link_up.matching_manager.data.remote.ArenaRemoteDataSource
import com.link_up.matching_manager.domain.model.ArenaEntity
import com.link_up.matching_manager.domain.model.toArenaEntity
import com.link_up.matching_manager.domain.repository.ArenaRepository

class ArenaRepositoryImpl(
    private val remoteDataSource: com.link_up.matching_manager.data.remote.ArenaRemoteDataSource
) : ArenaRepository {
    override suspend fun getArenaInfo(
        query: String,
        x: String,
        y: String,
        radius: Int,
        page: Int,
        size: Int,
        sort: String,
    ): ArenaEntity = remoteDataSource.getArenaInfo(
        query,
        x,
        y,
        radius,
        page,
        size,
        sort
    ).toArenaEntity()
}


