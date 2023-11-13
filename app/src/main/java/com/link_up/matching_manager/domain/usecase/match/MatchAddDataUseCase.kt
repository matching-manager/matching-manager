package com.link_up.matching_manager.domain.usecase.match

import com.google.firebase.database.DatabaseReference
import com.link_up.matching_manager.domain.repository.MatchRepository
import com.link_up.matching_manager.ui.match.MatchDataModel
import com.google.firebase.database.FirebaseDatabase

class MatchAddDataUseCase(val repository: MatchRepository)  {
    suspend operator fun invoke(databaseRef : DatabaseReference, data: MatchDataModel) = repository.addData(databaseRef, data)
}