package com.example.matching_manager.ui.my

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Image(
    val image: String,
    val title: String,
    val channelId: String,
    val date: String,
    val channelName: String,
    val description: String
) : Parcelable
