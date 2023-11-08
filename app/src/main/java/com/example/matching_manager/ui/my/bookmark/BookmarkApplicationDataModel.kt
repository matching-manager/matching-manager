package com.example.matching_manager.ui.my.bookmark

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class BookmarkApplicationDataModel(
    val type: String = "용병신청",
    val teamId: String = "",
    val userId: String = "testUser",
    val nickname: String = "또 너냐 손현준?",
    val userImg: String = "",
    val userEmail: String = "",
    val phoneNum: String = "",
    val fcmToken: String = "",
    var description: String = "아오 졸려",
    var gender: String = "남성",
    var chatCount: Int = 0,
    var level: String = "중(Lv4-6)",
    var playerNum: Int = 11,
    var postImg: String = "",
    var schedule: String = "",
    val uploadTime: String = "",
    var viewCount: Int = 0,
    var game: String = "축구",
    var area: String = "경기도 안양시",
    var age: Int = 26,//나이
) : Parcelable