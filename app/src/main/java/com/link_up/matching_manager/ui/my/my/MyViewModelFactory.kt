package com.link_up.matching_manager.ui.my.my

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.link_up.matching_manager.data.repository.MatchRepositoryImpl
import com.link_up.matching_manager.data.repository.MyRepositoryImpl
import com.link_up.matching_manager.domain.usecase.match.MatchAutoGetListUseCase
import com.link_up.matching_manager.domain.usecase.match.MatchDeleteDataUseCase
import com.link_up.matching_manager.domain.usecase.match.MatchEditDataUseCase

class MyViewModelFactory() : ViewModelProvider.Factory{
    private val myRepository = MyRepositoryImpl()
    private val matchRepository = MatchRepositoryImpl()

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MyViewModel::class.java)) {
            return MyViewModel(
                myRepository,
                MatchAutoGetListUseCase(matchRepository),
                MatchDeleteDataUseCase(matchRepository),
                MatchEditDataUseCase(matchRepository)
            ) as T
        } else {
            throw IllegalArgumentException("Not found ViewModel class.")
        }
    }
}