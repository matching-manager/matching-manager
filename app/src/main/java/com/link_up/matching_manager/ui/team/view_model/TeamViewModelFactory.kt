package com.link_up.matching_manager.ui.team.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.link_up.matching_manager.data.repository.TeamRepositoryImpl

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