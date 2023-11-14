package com.link_up.matching_manager.ui.my.my

import android.content.Context
import androidx.lifecycle.ViewModel
import com.link_up.matching_manager.domain.usecase.sharedpreference.DeleteUserTypeUseCase

class MyViewModel(
    private val deleteUser: DeleteUserTypeUseCase,
    private val context: Context
) : ViewModel() {

    fun deleteUserData() {
        deleteUser(context)
    }
}