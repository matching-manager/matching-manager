package com.example.matching_manager.ui.home.arena.alarm

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class AlarmModel(
    val userId: String?,
    val body: String?,
    val phoneNumber: String?
): Parcelable