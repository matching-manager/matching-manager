package com.example.matching_manager.ui.home.arena

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.matching_manager.data.repository.ArenaRepositoryImpl
import com.example.matching_manager.domain.repository.ArenaRepository
import com.example.matching_manager.domain.usecase.GetArenaInfoUseCase
import com.example.matching_manager.retrofit.RetrofitClient

class ArenaViewModelFactory : ViewModelProvider.Factory {

    private val repository: ArenaRepository = ArenaRepositoryImpl(
        RetrofitClient.search
    )

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ArenaViewModel::class.java)) {
            return ArenaViewModel(
                GetArenaInfoUseCase(repository)
            ) as T
        } else {
            throw IllegalArgumentException("Not found ViewModel class.")
        }
    }
}