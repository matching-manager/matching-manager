package com.example.matching_manager.ui.team.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.matching_manager.R
import com.example.matching_manager.ui.team.TeamItem

class TeamViewModel : ViewModel() {

    private val _list: MutableLiveData<List<TeamItem>> = MutableLiveData()
    val list: LiveData<List<TeamItem>> get() = _list

    init {
        _list.value = arrayListOf<TeamItem>().apply {
            for (i in 0 until 2) {
                add(
                    TeamItem.RecruitmentItem(
                        "용병모집" +
                                "$i",
                        "",
                        "[풋살]",
                        "[경기도/광주]",
                        "11/2 (목) 오후 8시",
                        R.drawable.sonny,
                        "3명",
                        "5,000원",
                        "광주FC",
                        "남성",
                        5,
                        3,
                        "조선대학교 운동장",
                        "광주손흥민",
                        "이번주 목요일 8시 같이 경기 뛰실 용병 구합니다~",
                        "5시간전",
                        "중수"
                    )
                )
            }
        }
    }

    fun addContentItem(item: TeamItem, type: String?) {
        val currentList = list.value.orEmpty().toMutableList()
        currentList.add(item)
        _list.value = currentList
    }

    //아이템을 추가합니다.



}
