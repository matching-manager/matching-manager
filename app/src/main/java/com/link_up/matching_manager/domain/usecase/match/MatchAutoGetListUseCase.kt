package com.link_up.matching_manager.domain.usecase.match

import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.FirebaseDatabase
import com.link_up.matching_manager.domain.repository.MatchRepository
import com.link_up.matching_manager.ui.match.MatchDataModel

class MatchAutoGetListUseCase(val repository: MatchRepository) {
    operator fun invoke(database: FirebaseDatabase, list : MutableLiveData<List<MatchDataModel>>) = repository.autoGetList(database, list)
}