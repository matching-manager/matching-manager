package com.link_up.matching_manager.ui.my.my

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.link_up.matching_manager.data.repository.MatchRepositoryImpl
import com.link_up.matching_manager.data.repository.MyRepositoryImpl
import com.link_up.matching_manager.data.repository.TeamRepositoryImpl
import com.link_up.matching_manager.domain.usecase.match.MatchAutoGetListUseCase
import com.link_up.matching_manager.domain.usecase.match.MatchDeleteDataUseCase
import com.link_up.matching_manager.domain.usecase.match.MatchEditDataUseCase
import com.link_up.matching_manager.domain.usecase.team.TeamAutoGetApplicationListUseCase
import com.link_up.matching_manager.domain.usecase.team.TeamAutoGetRecruitListUseCase
import com.link_up.matching_manager.domain.usecase.team.TeamDeleteApplicationDataUseCase
import com.link_up.matching_manager.domain.usecase.team.TeamDeleteRecruitDataUseCase
import com.link_up.matching_manager.domain.usecase.team.TeamEditApplicationDataUseCase
import com.link_up.matching_manager.domain.usecase.team.TeamEditRecruitDataUseCase

class MyViewModelFactory() : ViewModelProvider.Factory{
    private val matchRepository = MatchRepositoryImpl()
    private val teamRepository = TeamRepositoryImpl()

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MyViewModel::class.java)) {
            return MyViewModel(
                MatchAutoGetListUseCase(matchRepository),
                MatchDeleteDataUseCase(matchRepository),
                MatchEditDataUseCase(matchRepository),
                TeamAutoGetRecruitListUseCase(teamRepository),
                TeamAutoGetApplicationListUseCase(teamRepository),
                TeamDeleteRecruitDataUseCase(teamRepository),
                TeamDeleteApplicationDataUseCase(teamRepository),
                TeamEditRecruitDataUseCase(teamRepository),
                TeamEditApplicationDataUseCase(teamRepository)
            ) as T
        } else {
            throw IllegalArgumentException("Not found ViewModel class.")
        }
    }
}