package com.example.matching_manager.ui.match

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MatchViewModel(private val repository: MatchRepository) : ViewModel() {

    private val _list : MutableLiveData<MutableList<MatchDataModel>> = MutableLiveData()

    val list : MutableLiveData<MutableList<MatchDataModel>>
        get() =  _list

    fun updateList() {
        _list.value = repository.getList()
    }

}