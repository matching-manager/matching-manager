package com.example.matching_manager.ui.team

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
sealed class TeamItem(
    open val teamId: String = "",   //게시글ID
    open val userId: String = "testUser",   //유저ID
    open val userImg: Int = 0,  //유저 이미지
    open val nickname: String = "또 너냐 손현준?",//닉네임
    open val type: String = "",//용병모집
    open var game: String = "축구",//경기종목
    open var area: String = "경기도 안양시",//지역
    open var schedule: String = "11월 1일 저녁",//가능시간 월,일,시간
    open var playerNum: Int = 3,//인원
    open var viewCount: Int = 0,//조회수
    open var chatCount: Int = 0,//채팅수
    open var description: String = "",//글내용
    open val uploadTime: String = "",//작성시간
    open var level: String = "중", //수준
    open var postImg: String = "",  //게시글 사진
    open var gender: String = "남성",//성별
) : Parcelable {
    data class RecruitmentItem(
        override val type: String = "용명모집",
        override val teamId: String = "",
        override val userId: String = "testUser",
        override val nickname: String = "또 너냐 손현준?",
        override val userImg: Int = 0,
        override var description: String = "아오 졸려",
        override var gender: String = "남성",
        override var chatCount: Int = 0,
        override var level: String = "중(Lv4-6)",
        override var playerNum: Int = 11,
        override var postImg: String = "",
        override var schedule: String = "",
        override val uploadTime: String = "",
        override var viewCount: Int = 0,
        override var game: String = "축구",
        override var area: String = "경기도 안양시",
        var pay: Int = 5000,//참가비
        var teamName: String = "참다랑어FC"//팀이름
    ) : TeamItem(
        teamId,
        userId,
        userImg,
        nickname,
        type,
        game,
        area,
        schedule,
        playerNum,
        viewCount,
        chatCount,
        description,
        uploadTime,
        level,
        postImg,
        gender
    ), Parcelable

    data class ApplicationItem(
        override val type: String = "용병신청",
        override val teamId: String = "",
        override val userId: String = "testUser",
        override val nickname: String = "또 너냐 손현준?",
        override val userImg: Int = 0,
        override var description: String = "아오 졸려",
        override var gender: String = "남성",
        override var chatCount: Int = 0,
        override var level: String = "중(Lv4-6)",
        override var playerNum: Int = 11,
        override var postImg: String = "",
        override var schedule: String = "",
        override val uploadTime: String = "",
        override var viewCount: Int = 0,
        override var game: String = "축구",
        override var area: String = "경기도 안양시",
        var age: Int = 26,//나이
    ) : TeamItem(
        teamId,
        userId,
        userImg,
        nickname,
        type,
        game,
        area,
        schedule,
        playerNum,
        viewCount,
        chatCount,
        description,
        uploadTime,
        level,
        postImg,
        gender
    ), Parcelable
}
