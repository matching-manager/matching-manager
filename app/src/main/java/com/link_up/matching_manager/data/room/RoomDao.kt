package com.link_up.matching_manager.data.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RoomDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMatch(match : MatchEntity)

    @Query("SELECT * FROM match_table")
    fun getAllMatch() : LiveData<List<MatchEntity>>

    @Query("DELETE FROM match_table WHERE matchId = :matchId")
    suspend fun deleteMatch(matchId : String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecruitment(recruitment : RecruitmentEntity)

    @Query("SELECT * FROM recruitment_table")
    fun getAllRecruitment() : LiveData<List<RecruitmentEntity>>

    @Query("DELETE FROM recruitment_table WHERE teamId = :teamId")
    suspend fun deleteRecruitment(teamId : String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertApplication(application : ApplicationEntity)

    @Query("SELECT * FROM application_table")
    fun getAllApplication() : LiveData<List<ApplicationEntity>>

    @Query("DELETE FROM application_table WHERE teamId = :teamId")
    suspend fun deleteApplication(teamId : String)

}