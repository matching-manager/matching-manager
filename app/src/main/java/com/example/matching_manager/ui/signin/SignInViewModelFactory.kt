package com.example.matching_manager.ui.signin

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.matching_manager.data.repository.SharedPreferenceRepositoryImpl
import com.example.matching_manager.data.repository.database.UserInfoRepositoryImpl
import com.example.matching_manager.domain.repository.SharedPreferenceRepository
import com.example.matching_manager.domain.repository.database.UserInfoRepository
import com.example.matching_manager.domain.usecase.database.AddUserInfoToDataBaseUseCase
import com.example.matching_manager.domain.usecase.database.CheckUserInfoToDataBaseUseCase
import com.example.matching_manager.domain.usecase.sharedpreference.CheckUserTypeUseCase
import com.example.matching_manager.domain.usecase.sharedpreference.SaveUserTypeUseCase

class SignInViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    private val userInfoRepository: UserInfoRepository = UserInfoRepositoryImpl()
    private val sharedPreferenceRepository : SharedPreferenceRepository = SharedPreferenceRepositoryImpl()

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SignInSharedViewModel::class.java)) {
            return SignInSharedViewModel(
                AddUserInfoToDataBaseUseCase(userInfoRepository),
                CheckUserInfoToDataBaseUseCase(userInfoRepository),
                SaveUserTypeUseCase(sharedPreferenceRepository),
                CheckUserTypeUseCase(sharedPreferenceRepository),
                context
            ) as T
        } else {
            throw IllegalArgumentException("Not found ViewModel class.")
        }
    }
}