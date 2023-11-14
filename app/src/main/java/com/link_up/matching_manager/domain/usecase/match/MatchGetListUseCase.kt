package com.link_up.matching_manager.domain.usecase.match

import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DatabaseReference
import com.link_up.matching_manager.domain.repository.MatchRepository
import com.google.firebase.database.FirebaseDatabase
import com.link_up.matching_manager.ui.match.MatchDataModel

class MatchGetListUseCase(val repository: MatchRepository) {
    operator fun invoke(databaseRef : DatabaseReference, originalList : MutableList<MatchDataModel>, list : MutableLiveData<List<MatchDataModel>>) = repository.getList(databaseRef, originalList, list)
}