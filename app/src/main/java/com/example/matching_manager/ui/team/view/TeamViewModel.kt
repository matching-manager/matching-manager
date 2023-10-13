package com.example.matching_manager.ui.team.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.matching_manager.R
import com.example.matching_manager.ui.team.TeamModel

class TeamViewModel : ViewModel() {

    private val _list: MutableLiveData<List<TeamModel>> = MutableLiveData()
    val list:LiveData<List<TeamModel>> get()=_list

    init {
        _list.value= arrayListOf<TeamModel>().apply {
            for (i in 0 until 5) {
                add(TeamModel(R.drawable.sonny, "용병모집", "2명 남성", 1, 0, "11/2 (목) 오후 8시", "경기도 안양시 평촌중앙공원 축구장"))
            }
        }
    }

}
