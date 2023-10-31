package com.example.matching_manager.ui.signin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.matching_manager.data.repository.database.UserInfoRepositoryImpl
import com.example.matching_manager.domain.repository.database.UserInfoRepository

class SignInViewModelFactory : ViewModelProvider.Factory {
    private val repository: UserInfoRepository = UserInfoRepositoryImpl()

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SignInViewModel::class.java)) {
            return SignInViewModel(
                repository
            ) as T
        } else {
            throw IllegalArgumentException("Not found ViewModel class.")
        }
    }
}