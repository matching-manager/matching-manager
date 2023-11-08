package com.link_up.matching_manager.domain.usecase.sharedpreference

import android.content.Context
import com.link_up.matching_manager.domain.repository.FcmRepository
import com.link_up.matching_manager.domain.repository.SharedPreferenceRepository

class CheckUserTypeUseCase(
    private val repository: SharedPreferenceRepository
) {
    operator fun invoke(context: Context): Boolean = repository.checkUserType(context)
}