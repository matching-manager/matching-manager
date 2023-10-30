package com.example.matching_manager.ui.team.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.matching_manager.R
import com.example.matching_manager.ui.team.TeamItem

class TeamViewModel : ViewModel() {

    private val _list: MutableLiveData<List<TeamItem>?> = MutableLiveData()
    private var originalList: MutableList<TeamItem> = mutableListOf() // 원본 데이터를 보관할 리스트
    val list: MutableLiveData<List<TeamItem>?> get() = _list
    private var isRecruitmentChecked = false
    private var isApplicationChecked = false


    init {
        originalList =
            arrayListOf<TeamItem>().apply {
                for (i in 1 until 2) {
                    add(
                        TeamItem.RecruitmentItem(
                            "용병모집",
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
                            "5시간 전",
                            "중(Lv4-6)",
                            "[풋살]",
                            "[경기도/광주]",
                        )
                    )
                    add(
                        TeamItem.ApplicationItem(
                            "용병신청",
                            "평일",
                            R.drawable.sonny,
                            "3명",
                            "20",
                            "여성",
                            0,
                            5, "뽑아줘",
                            "조선대학교 운동장",
                            "1일 전",
                            "중(Lv4-6)",
                            "[풋살]",
                            "[광주/동구]"
                        )

                    )
                }
            }

        _list.value = originalList
    }

    //글을 추가해주는 함수
    fun addContentItem(item: TeamItem) {
        originalList.add(item)
        clearFilter()
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
        isRecruitmentChecked = true
        if (isRecruitmentChecked) {
            _list.value = originalList.filterIsInstance<TeamItem.RecruitmentItem>()
        }
    }

    //용병신청만 필터링하는 함수
    fun filterApplicationItems() {
        isApplicationChecked = true
        if (isApplicationChecked) {
            _list.value = originalList.filterIsInstance<TeamItem.ApplicationItem>()
        }
    }

    fun filterItems(selectedGame: String?, selectedArea: String?) {
        if ("선택" in selectedGame.orEmpty() && "선택" in selectedArea.orEmpty()) {
            // 게임과 지역이 선택되지 않았을 경우, 필터링을 하지 않고 모든 아이템을 보여줍니다.
            _list.value = originalList
            return
        }
        val filteredList = originalList.filter { item ->
            // 선택된 게임 또는 지역이 있을 경우, 해당 조건에 맞는 아이템만 필터링합니다.
            val isGameMatched = selectedGame.isNullOrBlank() || (item.game.contains(
                selectedGame,
                ignoreCase = true
            ))
            Log.d("game match","${isGameMatched}")
            val isAreaMatched = selectedArea.isNullOrBlank() || (item.area.contains(
                selectedArea,
                ignoreCase = true
            ))
            Log.d("area match","${isAreaMatched}")
            isGameMatched || isAreaMatched
        }
        Log.d("filter match","${filteredList}")
        _list.value = filteredList

    }

    fun clearFilter() {
        _list.value = originalList // 현재 _list에 있는 값을 다시 할당하여 불러오기
    }
}


