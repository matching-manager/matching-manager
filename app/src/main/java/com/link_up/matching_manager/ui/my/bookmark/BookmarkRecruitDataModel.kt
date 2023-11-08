package com.link_up.matching_manager.ui.my.bookmark

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class BookmarkRecruitDataModel (
    val type: String = "용명모집",
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
    var pay: Int = 5000,//참가비
    var teamName: String = "참다랑어FC"//팀이름
) : Parcelable