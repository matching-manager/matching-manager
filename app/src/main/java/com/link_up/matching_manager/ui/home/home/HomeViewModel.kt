package com.link_up.matching_manager.ui.home.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.link_up.matching_manager.ui.match.MatchDataModel

class HomeViewModel : ViewModel() {
    // 공지사항 리스트
    // TODO : MatchDataModel을 새로운 모델 만들어서 바꾸기
    private val _announcementList : MutableLiveData<List<MatchDataModel>> = MutableLiveData()
    val announcementList : LiveData<List<MatchDataModel>> get() = _announcementList

    // 공지사항 모델 -> HomeFragment에서 공지사항 아이템을 클릭하면 바뀌는 모델로 AnnouncementFragment에서
    // TODO : MatchDataModel을 새로운 모델 만들어서 바꾸기
    private val _announceModel : MutableLiveData<MatchDataModel> = MutableLiveData()
    val announceModel : LiveData<MatchDataModel> get() = _announceModel


    // 추천 경기매칭 리스트
    private val _matchList : MutableLiveData<List<MatchDataModel>> = MutableLiveData()
    val matchList : LiveData<List<MatchDataModel>> get() = _matchList

}