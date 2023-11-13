package com.link_up.matching_manager.domain.usecase.api

import com.link_up.matching_manager.domain.model.ArenaEntity
import com.link_up.matching_manager.domain.repository.ArenaRepository
import javax.inject.Inject

class GetArenaInfoUseCase @Inject constructor(
    private val repository: ArenaRepository
) {
    suspend operator fun invoke(
        query: String,
        x: String,
        y: String,
        radius: Int = 20000,
        page: Int =  1,
        size: Int =  10,
        sort: String = "distance"
    ): ArenaEntity = repository.getArenaInfo(
        query,
        x,
        y,
        radius,
        page,
        size,
        sort,
    )
}