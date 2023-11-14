package com.link_up.matching_manager.domain.usecase.match

import com.google.firebase.database.DatabaseReference
import com.link_up.matching_manager.domain.repository.MatchRepository
import com.link_up.matching_manager.ui.match.MatchDataModel

class MatchDeleteDataUseCase(val repository: MatchRepository) {
    suspend operator fun invoke(databaseRef : DatabaseReference, data : MatchDataModel) = repository.deleteData(databaseRef, data)
}