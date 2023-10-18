package com.example.matching_manager.ui.my

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MyMatchDataModel(
    val matchId : Int = 20,
    val userId : String = "userId",    //유저ID
    var userImg: Int = 0,     //유저이미지
    var teamName : String = "team",  //팀이름
    var game : String = "type", //경기종류 (축구, 풋살, 농구, 배드민턴, 볼링)
    var schedule : String = "10월 20일 오후 8시",  //경기시간
    var matchPlace: String = "경기도 안양시 평촌 중앙공원 축구장", //경기장소
    var playerNum: Int = 11,  //경기인원
    var entryFee: Int = 10000,   //참가비
    var description : String = "정보",   //정보
    var gender : String = "남성",    //성별
    var postImg : Int = 0,      //게시글 사진
    var viewCount: Int = 5,     //조회수
    var chatCount: Int = 1,  //신청수
) : Parcelable