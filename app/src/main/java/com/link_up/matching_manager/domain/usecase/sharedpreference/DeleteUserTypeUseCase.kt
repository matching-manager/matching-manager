package com.link_up.matching_manager.domain.usecase.sharedpreference

import android.content.Context
import com.link_up.matching_manager.domain.repository.SharedPreferenceRepository

class DeleteUserTypeUseCase(
    private val repository: SharedPreferenceRepository
) {
    operator fun invoke(context: Context) = repository.deleteUserType(context)
}