package com.link_up.matching_manager.domain.usecase.match

import com.link_up.matching_manager.domain.repository.MatchRepository
import com.google.firebase.database.FirebaseDatabase

class MatchGetListUseCase(val repository: MatchRepository) {
    suspend operator fun invoke(database: FirebaseDatabase) = repository.getList(database)
}