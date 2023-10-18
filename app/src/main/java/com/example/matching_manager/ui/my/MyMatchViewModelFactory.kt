package com.example.matching_manager.ui.my

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.matching_manager.ui.match.MatchRepositoryImpl

class MyMatchViewModelFactory : ViewModelProvider.Factory{
    private val repository = MyMatchRepositoryImpl()

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MyViewModel::class.java)) {
            return MyViewModel(
                repository
            ) as T
        } else {
            throw IllegalArgumentException("Not found ViewModel class.")
        }
    }
}