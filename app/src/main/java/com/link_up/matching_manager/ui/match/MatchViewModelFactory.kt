package com.link_up.matching_manager.ui.match

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.link_up.matching_manager.data.repository.MatchRepositoryImpl
import com.link_up.matching_manager.domain.usecase.match.MatchAddDataUseCase
import com.link_up.matching_manager.domain.usecase.match.MatchAutoGetListUseCase
import com.link_up.matching_manager.domain.usecase.match.MatchEditChatCountUseCase
import com.link_up.matching_manager.domain.usecase.match.MatchEditViewCountUseCase
import com.link_up.matching_manager.domain.usecase.match.MatchGetListUseCase

class MatchViewModelFactory : ViewModelProvider.Factory {
    private val repository = MatchRepositoryImpl()

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MatchViewModel::class.java)) {
            return MatchViewModel(
                MatchAddDataUseCase(repository),
                MatchGetListUseCase(repository),
                MatchEditChatCountUseCase(repository),
                MatchEditViewCountUseCase(repository),
                MatchAutoGetListUseCase(repository)
            ) as T
        } else {
            throw IllegalArgumentException("Not found ViewModel class.")
        }
    }
}