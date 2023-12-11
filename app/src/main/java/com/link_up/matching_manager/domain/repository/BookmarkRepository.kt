package com.link_up.matching_manager.domain.repository

import androidx.lifecycle.LiveData
import com.link_up.matching_manager.data.room.ApplicationEntity
import com.link_up.matching_manager.data.room.MatchEntity
import com.link_up.matching_manager.data.room.RecruitmentEntity
import com.link_up.matching_manager.data.room.RoomDB

interface BookmarkRepository {
    fun getMatchBookmark(roomDB: RoomDB) : LiveData<List<MatchEntity>>

    suspend fun insertMatch(roomDB: RoomDB, match : MatchEntity)

    suspend fun deleteMatch(roomDB: RoomDB, matchId : String)

    fun getRecruitmentBookmark(roomDB: RoomDB) : LiveData<List<RecruitmentEntity>>

    suspend fun insertRecruitment(roomDB: RoomDB, recruitment : RecruitmentEntity)

    suspend fun deleteRecruitment(roomDB: RoomDB, teamId : String)

    fun getApplicationBookmark(roomDB: RoomDB) : LiveData<List<ApplicationEntity>>

    suspend fun insertApplication(roomDB: RoomDB, application : ApplicationEntity)

    suspend fun deleteApplication(roomDB: RoomDB, teamId : String)
}