package com.link_up.matching_manager.domain.usecase.match

import com.google.firebase.database.DatabaseReference
import com.link_up.matching_manager.domain.repository.MatchRepository
import com.link_up.matching_manager.ui.match.MatchDataModel
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query

class MatchEditViewCountUseCase(val repository: MatchRepository)  {
    suspend operator fun invoke(query: Query, data: MatchDataModel) = repository.editViewCount(query, data)
}