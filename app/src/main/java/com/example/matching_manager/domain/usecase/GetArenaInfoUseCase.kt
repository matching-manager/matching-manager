package com.example.matching_manager.domain.usecase

import com.example.matching_manager.domain.model.ArenaEntity
import com.example.matching_manager.domain.repository.ArenaRepository

class GetArenaInfoUseCase(
    private val repository: ArenaRepository
) {
    suspend operator fun invoke(
        query: String,
        x: String,
        y: String,
        radius: Int,
        page: Int,
        size: Int,
        sort: String,
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