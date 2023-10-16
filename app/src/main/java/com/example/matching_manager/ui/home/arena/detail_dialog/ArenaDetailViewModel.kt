package com.example.matching_manager.ui.home.arena.detail_dialog

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.matching_manager.ui.home.arena.ArenaModel

class ArenaDetailViewModel : ViewModel(){
    private val _detailItem: MutableLiveData<ArenaModel> = MutableLiveData()
    val detailItem : LiveData<ArenaModel> get() = _detailItem

    fun updateItemDetail(item : ArenaModel){
        _detailItem.value = item
    }

    fun getPhoneNumber() : String? {
        return detailItem.value?.phone.toString()
    }

    fun getPlaceUrl() : String{
        return detailItem.value?.placeUrl.toString()
    }
}