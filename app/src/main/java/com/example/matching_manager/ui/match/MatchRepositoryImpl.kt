package com.example.matching_manager.ui.match

import com.example.matching_manager.R

class MatchRepositoryImpl()  : MatchRepository{
    private val list = mutableListOf<MatchDataModel>()

    init {
        list.apply {
            for(i in 1..10) {
                add(MatchDataModel(1, "testUser", R.drawable.sonny, "수원 삼성", "축구", "11월2일 오후8시", "경기도 안양시 평촌 중앙공원 축구장", 11, 10000, "초보만 받습니다", "남성", R.drawable.sonny, 1, 0))
            }
        }
    }

    override suspend fun getList(): MutableList<MatchDataModel> {
        return list
    }
}