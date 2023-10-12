package com.example.matching_manager.data.remote

import com.example.matching_manager.data.model.ArenaResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ArenaRemoteDataSource {
    @GET("/v2/local/search/keyword")
    suspend fun getArenaInfo(
        @Query("query") query: String,
        @Query("x") x: String,
        @Query("y") y: Int,
        @Query("radius") radius: Int,
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("sort") sort: Int,
    ): ArenaResponse
}