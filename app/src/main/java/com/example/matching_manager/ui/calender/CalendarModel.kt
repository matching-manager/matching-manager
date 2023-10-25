package com.example.matching_manager.ui.calender

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class CalendarModel(
    val day: Int?,
    val month: Int,
    val year: Int?,
    val place: String?,
    val memo: String?
) : Parcelable