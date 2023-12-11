package com.link_up.matching_manager.ui.my.my

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.link_up.matching_manager.data.repository.BookmarkRepositoryImpl
import com.link_up.matching_manager.data.room.ApplicationEntity
import com.link_up.matching_manager.data.room.MatchEntity
import com.link_up.matching_manager.data.room.RecruitmentEntity
import com.link_up.matching_manager.data.room.RoomDB
import com.link_up.matching_manager.ui.match.MatchDataModel
import com.link_up.matching_manager.ui.team.TeamItem
import com.link_up.matching_manager.util.Utils.toApplicationEntity
import com.link_up.matching_manager.util.Utils.toMatchEntity
import com.link_up.matching_manager.util.Utils.toRecruitmentEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@SuppressLint("StaticFieldLeak")
class MyBookmarkViewModel(
    private val repository: BookmarkRepositoryImpl,
    application: Application
) : AndroidViewModel(application) {
    val bookmarkMatchList: LiveData<List<MatchEntity>> get() = repository.getMatchBookmark(db)

    val bookmarkRecruitList: LiveData<List<RecruitmentEntity>> get() = repository.getRecruitmentBookmark(db)

    val bookmarkApplicationList: LiveData<List<ApplicationEntity>> get() = repository.getApplicationBookmark(db)

    private val context = getApplication<Application>().applicationContext
    private val db = RoomDB.getDatabase(context)

    fun insertBookmarkMatchData(matchData: MatchDataModel) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertMatch(db, matchData.toMatchEntity())
        }
    }

    fun insertBookmarkRecruitmentData(recruitmentData : TeamItem.RecruitmentItem) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertRecruitment(db, recruitmentData.toRecruitmentEntity())
        }
    }

    fun insertBookmarkApplicationData(applicationData : TeamItem.ApplicationItem) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertApplication(db, applicationData.toApplicationEntity())
        }
    }

    fun deleteBookmarkMatchData(matchData: MatchDataModel) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteMatch(db, matchData.matchId)
        }
    }

    fun deleteBookmarkRecruitmentData(recruitmentData: TeamItem.RecruitmentItem) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteRecruitment(db, recruitmentData.teamId)
        }
    }

    fun deleteBookmarkApplicationData(applicationData: TeamItem.ApplicationItem) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteApplication(db, applicationData.teamId)
        }
    }
}