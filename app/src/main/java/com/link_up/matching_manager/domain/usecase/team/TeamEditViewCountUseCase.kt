package com.link_up.matching_manager.domain.usecase.team

import com.google.firebase.database.FirebaseDatabase
import com.link_up.matching_manager.domain.repository.TeamRepository
import com.link_up.matching_manager.ui.team.TeamItem

class TeamEditViewCountUseCase(val repository: TeamRepository) {
    suspend operator fun invoke(data: TeamItem, database: FirebaseDatabase) = repository.editViewCount(data, database)
}