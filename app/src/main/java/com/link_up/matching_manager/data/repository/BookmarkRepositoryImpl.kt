package com.link_up.matching_manager.data.repository

import androidx.lifecycle.LiveData
import com.link_up.matching_manager.data.room.ApplicationEntity
import com.link_up.matching_manager.data.room.MatchEntity
import com.link_up.matching_manager.data.room.RecruitmentEntity
import com.link_up.matching_manager.data.room.RoomDB
import com.link_up.matching_manager.domain.repository.BookmarkRepository

class BookmarkRepositoryImpl() : BookmarkRepository {

    override fun getMatchBookmark(roomDB: RoomDB) : LiveData<List<MatchEntity>> {
        return roomDB.roomDao().getAllMatch()
    }

    override suspend fun insertMatch(roomDB: RoomDB, match : MatchEntity) {
        roomDB.roomDao().insertMatch(match)
    }

    override suspend fun deleteMatch(roomDB: RoomDB, matchId : String) {
        roomDB.roomDao().deleteMatch(matchId)
    }

    override fun getRecruitmentBookmark(roomDB: RoomDB): LiveData<List<RecruitmentEntity>> {
        return roomDB.roomDao().getAllRecruitment()
    }

    override suspend fun insertRecruitment(roomDB: RoomDB, recruitment: RecruitmentEntity) {
        roomDB.roomDao().insertRecruitment(recruitment)
    }

    override suspend fun deleteRecruitment(roomDB: RoomDB, teamId: String) {
        roomDB.roomDao().deleteRecruitment(teamId)
    }

    override fun getApplicationBookmark(roomDB: RoomDB): LiveData<List<ApplicationEntity>> {
        return roomDB.roomDao().getAllApplication()
    }

    override suspend fun insertApplication(roomDB: RoomDB, application: ApplicationEntity) {
        roomDB.roomDao().insertApplication(application)
    }

    override suspend fun deleteApplication(roomDB: RoomDB, teamId: String) {
        roomDB.roomDao().deleteApplication(teamId)
    }
}