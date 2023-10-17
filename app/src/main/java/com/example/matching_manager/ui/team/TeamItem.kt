package com.example.matching_manager.ui.team

import android.os.Parcelable
import com.example.matching_manager.R
import kotlinx.android.parcel.Parcelize

@Parcelize
sealed class TeamItem(

) : Parcelable {
    data class RecruitmentItem(
        val type: String,//용병모집
        val detail:String,
        val game:String,//경기종목
        val area:String,//지역
        val schedule: String,//가능시간 월,일,시간
        val teamProfile: Int,//프로필
        val playerNum: String,//인원
        val pay:String,//참가비
        val teamName:String,//팀이름
        val gender: String,//성별
        var viewCount: Int,//조회수
        var chatCount: Int,//채팅수
        val place: String,//경기장위치
        val nicname:String,//닉네임
        val content:String,//글내용
        val creationTime:String,//작성시간
        val level:String//수준
    ) : TeamItem(), Parcelable

    data class ApplicationItem(
        val type: String,//용병신청
        val game:String,//경기종목
        val area:String,//지역
        val schedule: String,//시간 평일/주말
        val teamProfile: Int,//프로필
        val playerNum: String,//인원
        val age: String,//나이
        val gender: String,//성별
        var viewCount: Int,//조회수
        var chatCount: Int,//채팅수
        val nicname:String,//닉네임
        val content:String,//글내용
        val creationTime:String,//작성시간
        val level:String//수준
    ) : TeamItem(), Parcelable

}
