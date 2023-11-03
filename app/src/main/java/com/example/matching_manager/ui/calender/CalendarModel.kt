package com.example.matching_manager.ui.calender

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class CalendarModel(
    val day: Int?,
    val month: Int?,
    val year: Int?,
    var place: String?,
    var memo: String?,
    val schedule: String,
) : Parcelable