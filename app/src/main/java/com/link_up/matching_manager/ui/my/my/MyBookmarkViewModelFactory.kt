package com.link_up.matching_manager.ui.my.my

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.link_up.matching_manager.data.repository.BookmarkRepositoryImpl

class MyBookmarkViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    private val repository = BookmarkRepositoryImpl()

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MyBookmarkViewModel::class.java)) {
            return MyBookmarkViewModel(
                repository, application
            ) as T
        } else {
            throw IllegalArgumentException("Not found ViewModel class.")
        }
    }
}