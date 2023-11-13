package com.link_up.matching_manager.domain.usecase.team

import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.FirebaseDatabase
import com.link_up.matching_manager.domain.repository.TeamRepository
import com.link_up.matching_manager.ui.team.TeamItem

class TeamAutoGetListUseCase(val repository: TeamRepository) {
    operator fun invoke(database: FirebaseDatabase, list : MutableLiveData<List<TeamItem>>) = repository.autoGetList(database, list)
}