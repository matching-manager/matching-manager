package com.example.matching_manager.ui.match

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MatchDataModel(
    val matchId : Int,
    val userId : String,    //유저ID
    var userImg: Int,     //유저이미지
    var teamName : String,  //팀이름
    var matchType : String, //경기종류 (축구, 풋살, 농구, 탁구, 볼링)
    var dateTime : String,  //경기시간
    var matchPlace: String, //경기장소
    var playerNum: Int,  //경기인원
    var entryFee: Int,   //참가비
    var description : String,   //정보
    var gender : String,    //성별
    var postImg : Int,      //게시글 사진
    var viewCount: Int,     //조회수
    var applyCount: Int,  //신청수
) : Parcelable