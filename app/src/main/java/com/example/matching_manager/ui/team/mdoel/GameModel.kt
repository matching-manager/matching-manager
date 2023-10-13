package com.example.matching_manager.ui.team.mdoel

import android.os.Parcelable
import com.example.matching_manager.ui.team.TeamFragment
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GameModel(
    val gametitle: String,
) : Parcelable