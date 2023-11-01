package com.example.matching_manager.ui.signin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.matching_manager.data.model.UserInfoModel
import com.example.matching_manager.domain.repository.database.UserInfoRepository
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class SignInSharedViewModel(val repository: UserInfoRepository) : ViewModel() {

    private val _userType: MutableLiveData<String> = MutableLiveData()
    val userType: LiveData<String> get() = _userType


    private val database =
        Firebase.database("https://matching-manager-default-rtdb.asia-southeast1.firebasedatabase.app/")

    fun checkUserinfo(userInfoModel: UserInfoModel) {
        viewModelScope.launch {
            when(repository.checkUser(userInfoModel, database)){
                true ->{ //기존 유저
                    _userType.value = CheckUserType.EXISTING_USER.name
                }
                false ->{
                    _userType.value = CheckUserType.NEW_USER.name
                }
            }
        }
    }

    fun addUserInfo(userInfoModel: UserInfoModel) {
        viewModelScope.launch {
            repository.adduser(userInfoModel, database)
        }
    }

}