package com.example.matching_manager.ui.team.view

import android.os.Parcelable
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
            for (i in 0 until 5) {
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

    private fun createItems(
    ): List<TeamItem> {
        fun createRecruitItems(): List<TeamItem.RecruitmentItem> {
            val list= arrayListOf<TeamItem.RecruitmentItem>()
            list.apply {
                for (i in 0 until 5) {
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
            return list
        }

        fun createApplicationItems(): List<TeamItem.ApplicationItem> {
            val list= arrayListOf<TeamItem.ApplicationItem>()
            list.apply {
                for (i in 0 until 5) {
                    add(
                        TeamItem.ApplicationItem(
                            "용병신청" +
                                    "$i",
                            "[풋살]",
                            "[경기도/광주]",
                            "11/2 (목) 오후 8시",
                            R.drawable.sonny,
                            "3명",
                            "20살",
                            "남성",
                            5,
                            3,
                            "잘하고싶당",
                            "아무곳이나 들어가고싶어요 넣어주세요",
                            "5시간 전",
                            "중수",
                        )
                    )
                }
            }
            return list
        }

        // merge
        val items = arrayListOf<TeamItem>().apply {
            addAll(createRecruitItems())
            addAll(createApplicationItems())
        }

        return items
    }



}
