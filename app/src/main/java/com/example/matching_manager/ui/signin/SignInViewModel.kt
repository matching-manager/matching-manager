package com.example.matching_manager.ui.signin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.matching_manager.data.model.UserInfoModel
import com.example.matching_manager.domain.repository.database.UserInfoRepository
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class SignInViewModel(val repository: UserInfoRepository) : ViewModel() {

    private val database = Firebase.database("https://matching-manager-default-rtdb.asia-southeast1.firebasedatabase.app/")


    fun addUserInfo(userInfoModel: UserInfoModel) {
        viewModelScope.launch {
            repository.adduser(userInfoModel,database)
        }
    }

}