package com.link_up.matching_manager.ui.team.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.link_up.matching_manager.data.repository.TeamRepositoryImpl
import com.link_up.matching_manager.domain.usecase.team.TeamAddApplicationDataUseCase
import com.link_up.matching_manager.domain.usecase.team.TeamAddRecruitmentDataUseCase
import com.link_up.matching_manager.domain.usecase.team.TeamAutoGetListUseCase
import com.link_up.matching_manager.domain.usecase.team.TeamEditChatCountUseCase
import com.link_up.matching_manager.domain.usecase.team.TeamEditViewCountUseCase

class TeamViewModelFactory() : ViewModelProvider.Factory {
    private val repository = TeamRepositoryImpl()

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TeamViewModel::class.java)) {
            return TeamViewModel(
                TeamAutoGetListUseCase(repository),
                TeamAddApplicationDataUseCase(repository),
                TeamAddRecruitmentDataUseCase(repository),
                TeamEditChatCountUseCase(repository),
                TeamEditViewCountUseCase(repository)
            ) as T
        } else {
            throw IllegalArgumentException("Not found ViewModel class.")
        }
    }
}