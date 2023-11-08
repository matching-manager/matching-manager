package com.link_up.matching_manager.domain.usecase.match

import com.link_up.matching_manager.domain.repository.MatchRepository
import com.link_up.matching_manager.ui.match.MatchDataModel
import com.google.firebase.database.FirebaseDatabase

class MatchEditViewCountUseCase(val repository: MatchRepository)  {
    suspend operator fun invoke(data: MatchDataModel, database: FirebaseDatabase) = repository.editViewCount(data, database)
}