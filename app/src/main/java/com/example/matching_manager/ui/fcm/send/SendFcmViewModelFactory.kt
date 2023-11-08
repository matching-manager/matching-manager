package com.example.matching_manager.ui.fcm.send

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.matching_manager.data.repository.FcmRepositoryImpl
import com.example.matching_manager.domain.repository.FcmRepository
import com.example.matching_manager.retrofit.fcm.FcmRetrofitClient

class SendFcmViewModelFactory: ViewModelProvider.Factory{
    private val repository: FcmRepository = FcmRepositoryImpl(
        FcmRetrofitClient.api
    )

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SendFcmViewModel::class.java)) {
            return SendFcmViewModel(
                repository
            ) as T
        } else {
            throw IllegalArgumentException("Not found ViewModel class.")
        }
    }
}