package com.link_up.matching_manager.data.remote

import com.link_up.matching_manager.data.model.ArenaResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface ArenaRemoteDataSource {
    @GET("/v2/local/search/keyword.json")
    suspend fun getArenaInfo(
        @Query("query") query: String,
        @Query("x") x: String,
        @Query("y") y: String,
        @Query("radius") radius: Int,
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("sort") sort: String,
    ): com.link_up.matching_manager.data.model.ArenaResponse
}