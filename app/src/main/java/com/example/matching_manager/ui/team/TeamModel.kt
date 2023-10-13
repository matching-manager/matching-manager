package com.example.matching_manager.ui.team

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TeamModel(
    val teamProfile: Int,
    val type: String,
    val detail: String,
    var viewCount: Int,
    var chatCount: Int,
    val schedule: String,
    val place: String
) : Parcelable
