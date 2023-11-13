package com.link_up.matching_manager.domain.usecase.match

import androidx.lifecycle.MutableLiveData
import com.link_up.matching_manager.domain.repository.MatchRepository
import com.google.firebase.database.FirebaseDatabase
import com.link_up.matching_manager.ui.match.MatchDataModel

class MatchGetListUseCase(val repository: MatchRepository) {
    operator fun invoke(database : FirebaseDatabase, originalList : MutableList<MatchDataModel>, list : MutableLiveData<List<MatchDataModel>>) = repository.getList(database, originalList, list)
}