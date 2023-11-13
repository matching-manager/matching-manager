package com.link_up.matching_manager.ui.my.my

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.link_up.matching_manager.domain.repository.MyRepository
import com.link_up.matching_manager.domain.usecase.match.MatchAutoGetListUseCase
import com.link_up.matching_manager.domain.usecase.match.MatchDeleteDataUseCase
import com.link_up.matching_manager.domain.usecase.match.MatchEditDataUseCase
import com.link_up.matching_manager.ui.match.MatchDataModel
import com.link_up.matching_manager.ui.team.TeamItem
import com.link_up.matching_manager.util.UserInformation
import kotlinx.coroutines.launch

class MyViewModel(
    private val repository: MyRepository,
    val autoGetMatchList: MatchAutoGetListUseCase,
    val deleteData: MatchDeleteDataUseCase,
    val editData : MatchEditDataUseCase
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
            deleteData(matchRef, data)
            _event.postValue(MyEvent.Dismiss)
        }
    }

    fun editMatch(data: MatchDataModel, newData: MatchDataModel) {
        viewModelScope.launch {
            editData(matchRef, data, newData)
            _event.postValue(MyEvent.Finish)
        }
    }

    fun autoFetchRecruitData() {
        repository.getRecruitList(database, _recruitList)
    }

    fun deleteRecruit(data: TeamItem.RecruitmentItem) {
        viewModelScope.launch {
            repository.deleteRecruitData(data, database)
            _event.postValue(MyEvent.Dismiss)
        }
    }

    fun editRecruit(data: TeamItem.RecruitmentItem, newData: TeamItem.RecruitmentItem) {
        viewModelScope.launch {
            repository.editRecruitData(data, newData, database)
            _event.postValue(MyEvent.Finish)
        }
    }

    fun autoFetchApplicationData() {
        repository.getApplicationList(database, _applicationList)
    }

    fun deleteApplication(data: TeamItem.ApplicationItem) {
        viewModelScope.launch {
            repository.deleteApplicationData(data, database)
            _event.postValue(MyEvent.Dismiss)
        }
    }

    fun editApplication(data: TeamItem.ApplicationItem, newData: TeamItem.ApplicationItem) {
        viewModelScope.launch {
            repository.editApplicationData(data, newData, database)
            _event.postValue(MyEvent.Finish)
        }
    }
}

sealed interface MyEvent {
    object Dismiss : MyEvent
    object Finish : MyEvent
}

