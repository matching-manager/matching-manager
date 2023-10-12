package com.example.matching_manager.domain.repository

import com.example.matching_manager.domain.model.ArenaEntity
import retrofit2.http.Query

interface ArenaRepository {
    suspend fun getArenaInfo(
        query: String,
        x: String,
        y: Int,
        radius: Int,
        page: Int,
        size: Int,
        sort: Int,
    ): ArenaEntity
}