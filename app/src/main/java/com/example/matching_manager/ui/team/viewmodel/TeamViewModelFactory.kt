package com.example.matching_manager.ui.team.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class TeamViewModelFactory() : ViewModelProvider.Factory {
    private val repository = TeamRepositoryImpl()

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TeamViewModel::class.java)) {
            return TeamViewModel(
                repository
            ) as T
        } else {
            throw IllegalArgumentException("Not found ViewModel class.")
        }
    }
}