package com.example.matching_manager.ui.team.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.matching_manager.R
import com.example.matching_manager.ui.team.TeamAddType
import com.example.matching_manager.ui.team.TeamItem

class TeamViewModel : ViewModel() {

    private val _list: MutableLiveData<List<TeamItem>?> = MutableLiveData()
    private var filteredList: List<TeamItem>? = null
    val list: MutableLiveData<List<TeamItem>?> get() = _list


    init {

        // 이 부분에서 originalList를 초기화해주세요.
        filteredList = _list.value

        _list.value = arrayListOf<TeamItem>().apply {
            for (i in 1 until 2) {
                add(
                    TeamItem.RecruitmentItem(
                        "용병모집",
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
                        "23/10/11",
                        "중수(Lv4-6)"
                    )

                )
                add(
                    TeamItem.ApplicationItem(
                        "용병신청",
                        "[풋살]",
                        "[광주/동구]",
                        "평일",
                        R.drawable.sonny,
                        "3명",
                        "20",
                        "여성",
                        0,
                        5, "뽑아줘",
                        "조선대학교 운동장",
                        "23/10/20",
                        "중수(Lv4-6)",
                    )

                )
            }
        }
    }

    //글을 추가해주는 함수
    fun addContentItem(item: TeamItem) {
        val currentList = list.value.orEmpty().toMutableList()
        currentList.add(item)
        _list.value = currentList
    }

    //조회수를 업데이트하는 함수
    fun incrementViewCount(item: TeamItem) {
        if (item is TeamItem.RecruitmentItem) {
            val currentList = list.value.orEmpty().toMutableList()
            val updatedItem = item.copy(viewCount = item.viewCount + 1)
            val index = currentList.indexOf(item)
            if (index != -1) {
                currentList[index] = updatedItem
                _list.value = currentList
            }
        } else if (item is TeamItem.ApplicationItem) {
            val currentList = list.value.orEmpty().toMutableList()
            val updatedItem = item.copy(viewCount = item.viewCount + 1)
            val index = currentList.indexOf(item)
            if (index != -1) {
                currentList[index] = updatedItem
                _list.value = currentList
            }
        }
    }

    // 용병모집만 필터링하는 함수
    fun filterRecruitmentItems() {
        val recruitmentList = list.value.orEmpty().filterIsInstance<TeamItem.RecruitmentItem>()
        filteredList = list.value // 현재 보여지고 있는 리스트를 보관
        _list.value = recruitmentList
    }

    fun filterApplicationItems() {
        val applicationList = list.value.orEmpty().filterIsInstance<TeamItem.ApplicationItem>()
        filteredList = list.value // 현재 보여지고 있는 리스트를 보관
        _list.value = applicationList
    }

    fun clearFilter() {
        // 필터를 제거하고 이전에 보관해둔 리스트를 다시 보여준다.
        _list.value = filteredList
        filteredList = null // 보관해둔 리스트를 초기화
    }

}
