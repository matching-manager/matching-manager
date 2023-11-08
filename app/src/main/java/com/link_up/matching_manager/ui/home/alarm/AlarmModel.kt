package com.link_up.matching_manager.ui.home.alarm

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class AlarmModel(
    val userId: String?,
    val body: String?,
    val phoneNumber: String?
): Parcelable