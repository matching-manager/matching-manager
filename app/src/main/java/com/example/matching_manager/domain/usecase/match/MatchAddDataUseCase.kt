package com.example.matching_manager.domain.usecase.match

import com.example.matching_manager.domain.repository.MatchRepository
import com.example.matching_manager.ui.match.MatchDataModel
import com.google.firebase.database.FirebaseDatabase

class MatchAddDataUseCase(val repository: MatchRepository)  {
    suspend operator fun invoke(data: MatchDataModel, database: FirebaseDatabase) = repository.addData(data,database)
}