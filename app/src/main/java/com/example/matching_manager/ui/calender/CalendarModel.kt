package com.example.matching_manager.ui.calender

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class CalendarModel (
    val day: String?,
    val month: String?,
    val place: String?,
    val division: String?,
    val memo: String?
) : Parcelable