package com.link_up.matching_manager.data.room

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "match_table")
@Parcelize
data class MatchEntity(
    @PrimaryKey
    val matchId: String,
    val userId: String,
    val userNickname: String,
    val userImg: String,
    val userEmail: String,
    val fcmToken: String,
    val phoneNum: String,
    val teamName: String,
    val game: String,
    val schedule: String,
    val matchPlace: String,
    val playerNum: Int,
    val entryFee: Int,
    val description: String,
    val gender: String,
    val level: String,
    val postImg: String,
    val viewCount: Int,
    val chatCount: Int,
    val uploadTime: String,
) : Parcelable

@Entity(tableName = "recruitment_table")
@Parcelize
data class RecruitmentEntity(
    val type: String,
    @PrimaryKey
    val teamId: String,
    val userId: String,
    val nickname: String,
    val userImg: String,
    val userEmail: String,
    val phoneNum: String,
    val fcmToken: String,
    val description: String,
    val gender: String,
    val chatCount: Int,
    val level: String,
    val playerNum: Int,
    val postImg: String,
    val schedule: String,
    val uploadTime: String,
    val viewCount: Int,
    val game: String,
    val area: String,
    var pay: Int,
    var teamName: String
) : Parcelable

@Entity(tableName = "application_table")
@Parcelize
data class ApplicationEntity(
    val type: String,
    @PrimaryKey
    val teamId: String,
    val userId: String,
    val nickname: String,
    val userImg: String,
    val userEmail: String,
    val phoneNum: String,
    val fcmToken: String,
    val description: String,
    val gender: String,
    val chatCount: Int,
    val level: String,
    val playerNum: Int,
    val postImg: String,
    val schedule: String,
    val uploadTime: String,
    val viewCount: Int,
    val game: String,
    val area: String,
    val age: Int
) : Parcelable