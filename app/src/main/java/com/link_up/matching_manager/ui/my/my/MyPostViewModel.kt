package com.link_up.matching_manager.ui.my.my

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.link_up.matching_manager.domain.usecase.match.MatchAutoGetListUseCase
import com.link_up.matching_manager.domain.usecase.match.MatchDeleteDataUseCase
import com.link_up.matching_manager.domain.usecase.match.MatchEditDataUseCase
import com.link_up.matching_manager.domain.usecase.team.TeamAutoGetApplicationListUseCase
import com.link_up.matching_manager.domain.usecase.team.TeamAutoGetRecruitListUseCase
import com.link_up.matching_manager.domain.usecase.team.TeamDeleteDataUseCase
import com.link_up.matching_manager.domain.usecase.team.TeamEditDataUseCase
import com.link_up.matching_manager.ui.match.MatchDataModel
import com.link_up.matching_manager.ui.team.TeamItem
import com.link_up.matching_manager.util.UserInformation
import kotlinx.coroutines.launch

class MyPostViewModel(
    val autoGetMatchList: MatchAutoGetListUseCase,
    val deleteMatchData: MatchDeleteDataUseCase,
    val editMatchData : MatchEditDataUseCase,
    val autoGetRecruitList: TeamAutoGetRecruitListUseCase,
    val autoGetApplicationList: TeamAutoGetApplicationListUseCase,
    val deleteTeamData: TeamDeleteDataUseCase,
    val editTeamData: TeamEditDataUseCase
) : ViewModel() {

    private val _matchList: MutableLiveData<List<MatchDataModel>> = MutableLiveData()
    val matchList: LiveData<List<MatchDataModel>> get() = _matchList

    private val _recruitList: MutableLiveData<List<TeamItem.RecruitmentItem>> = MutableLiveData()
    val recruitList: LiveData<List<TeamItem.RecruitmentItem>> get() = _recruitList

    private val _applicationList: MutableLiveData<List<TeamItem.ApplicationItem>> =
        MutableLiveData()
    val applicationList: LiveData<List<TeamItem.ApplicationItem>> get() = _applicationList

    private val _bookmarkMatchList: MutableLiveData<List<MatchDataModel>> = MutableLiveData()
    val bookmarkMatchList: LiveData<List<MatchDataModel>> get() = _bookmarkMatchList

    private val _bookmarkRecruitList: MutableLiveData<List<TeamItem.RecruitmentItem>> =
        MutableLiveData()
    val bookmarkRecruitList: LiveData<List<TeamItem.RecruitmentItem>> get() = _bookmarkRecruitList

    private val _bookmarkApplicationList: MutableLiveData<List<TeamItem.ApplicationItem>> =
        MutableLiveData()
    val bookmarkApplicationList: LiveData<List<TeamItem.ApplicationItem>> get() = _bookmarkApplicationList

    private val _event: MutableLiveData<MyEvent> = MutableLiveData()
    val event: LiveData<MyEvent> get() = _event

    private val database =
        Firebase.database("https://matching-manager-default-rtdb.asia-southeast1.firebasedatabase.app/")
    private val matchRef = database.getReference("Match")
    private val matchQuery = matchRef.orderByChild("userId").equalTo(UserInformation.userInfo.uid)
    private val teamRef = database.getReference("Team")

    fun addBookmarkMatchLiveData(dataList: MutableList<MatchDataModel>) {
        _bookmarkMatchList.value = dataList
    }

    fun addBookmarkRecruitLiveData(dataList: MutableList<TeamItem.RecruitmentItem>) {
        _bookmarkRecruitList.value = dataList
    }

    fun addBookmarkApplicationLiveData(dataList: MutableList<TeamItem.ApplicationItem>) {
        _bookmarkApplicationList.value = dataList
    }

    fun autoFetchMatchData() {
        autoGetMatchList(null, matchQuery, _matchList)
    }

    fun deleteMatch(data: MatchDataModel) {
        viewModelScope.launch {
            deleteMatchData(matchRef, data)
            _event.postValue(MyEvent.Dismiss)
        }
    }

    fun editMatch(data: MatchDataModel, newData: MatchDataModel) {
        viewModelScope.launch {
            editMatchData(matchRef, data, newData)
            _event.postValue(MyEvent.Finish)
        }
    }

    fun autoFetchRecruitData() {
        autoGetRecruitList(teamRef, _recruitList)
    }

    fun deleteTeam(data: TeamItem) {
        viewModelScope.launch {
            deleteTeamData(teamRef, data)
            _event.postValue(MyEvent.Dismiss)
        }
    }

    fun editTeam(data: TeamItem, newData: TeamItem) {
        viewModelScope.launch {
            editTeamData(teamRef, data, newData)
            _event.postValue(MyEvent.Finish)
        }
    }

    fun autoFetchApplicationData() {
        autoGetApplicationList(teamRef, _applicationList)
    }
}

sealed interface MyEvent {
    object Dismiss : MyEvent
    object Finish : MyEvent
}

