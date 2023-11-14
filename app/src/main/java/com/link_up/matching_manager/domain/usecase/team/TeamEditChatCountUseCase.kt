package com.link_up.matching_manager.domain.usecase.team

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.link_up.matching_manager.domain.repository.TeamRepository
import com.link_up.matching_manager.ui.team.TeamItem

class TeamEditChatCountUseCase(val repository: TeamRepository) {
    suspend operator fun invoke(databaseRef : DatabaseReference, data: TeamItem) = repository.editChatCount(databaseRef, data)
}