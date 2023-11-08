package com.example.matching_manager.ui.my.match

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.matching_manager.data.repository.MyRepositoryImpl
import com.example.matching_manager.ui.my.my.MyViewModel

class MyMatchViewModelFactory() : ViewModelProvider.Factory{
    private val repository = MyRepositoryImpl()

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