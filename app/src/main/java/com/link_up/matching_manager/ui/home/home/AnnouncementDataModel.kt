package com.link_up.matching_manager.ui.home.home

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AnnouncementDataModel(
    val announceNum : Int = 0,
    var title: String = "",
    var content: String = "",
    var uploadDate: String = ""
) : Parcelable