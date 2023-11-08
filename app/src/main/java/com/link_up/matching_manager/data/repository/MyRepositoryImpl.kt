package com.link_up.matching_manager.data.repository

import com.link_up.matching_manager.ui.match.MatchDataModel
import com.link_up.matching_manager.domain.repository.MyRepository
import com.link_up.matching_manager.ui.team.TeamItem
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await

class MyRepositoryImpl() : MyRepository {

    override suspend fun getMatchList(userId : String, database: FirebaseDatabase): List<MatchDataModel> {
        val matchRef = database.getReference("Match")
        val items = arrayListOf<MatchDataModel>()
        val query = matchRef.orderByChild("userId").equalTo(userId)
        val snapshot = query.get().await()
        if (snapshot.exists()) {
            for (childSnapshot in snapshot.children) {
                childSnapshot.getValue(MatchDataModel::class.java)?.let { matchData ->
                    items.add(matchData)
                }
            }
        }
        return items
    }

    override suspend fun deleteMatchData(data: MatchDataModel, database: FirebaseDatabase) {
        val matchRef = database.getReference("Match")
        val query = matchRef.orderByChild("matchId").equalTo(data.matchId)
        val snapshot = query.get().await()
        if (snapshot.exists()) {
            for (childSnapshot in snapshot.children) {
                childSnapshot.ref.removeValue()
            }
        }
    }

    override suspend fun editMatchData(data: MatchDataModel, newData: MatchDataModel, database: FirebaseDatabase) {
        val matchRef = database.getReference("Match")
        val query = matchRef.orderByChild("matchId").equalTo(data.matchId)

        val dataToUpdate = hashMapOf(
            "teamName" to newData.teamName,
            "game" to newData.game,
            "schedule" to newData.schedule,
            "playerNum" to newData.playerNum,
            "matchPlace" to newData.matchPlace,
            "gender" to newData.gender,
            "level" to newData.level,
            "entryFee" to newData.entryFee,
            "description" to newData.description,
            "postImg" to newData.postImg
        )
        val snapshot = query.get().await()
        if (snapshot.exists()) {
            for (childSnapshot in snapshot.children) {
                childSnapshot.ref.updateChildren(dataToUpdate as Map<String, Any>)
            }
        }
    }

    override suspend fun getRecruitList(userId: String, database: FirebaseDatabase): List<TeamItem.RecruitmentItem> {
        val teamRef = database.getReference("Team")
        val items = arrayListOf<TeamItem.RecruitmentItem>()
        val snapshot = teamRef.get().await()
        if (snapshot.exists()) {
            for (childSnapshot in snapshot.children) {
                val recruitData = childSnapshot.getValue(TeamItem.RecruitmentItem::class.java)
                if (recruitData != null && recruitData.userId == userId && recruitData.type == "용병모집") {
                    items.add(recruitData)
                }
            }
        }
        return items
    }

    override suspend fun deleteRecruitData(data: TeamItem.RecruitmentItem, database: FirebaseDatabase) {
        val teamRef = database.getReference("Team")
        val query = teamRef.orderByChild("teamId").equalTo(data.teamId)
        val snapshot = query.get().await()
        if (snapshot.exists()) {
            for (childSnapshot in snapshot.children) {
                childSnapshot.ref.removeValue()
            }
        }
    }

    override suspend fun editRecruitData(data: TeamItem.RecruitmentItem, newData: TeamItem.RecruitmentItem, database: FirebaseDatabase) {
        val teamRef = database.getReference("Team")
        val query = teamRef.orderByChild("teamId").equalTo(data.teamId)

        val dataToUpdate = hashMapOf(
            "teamName" to newData.teamName,
            "game" to newData.game,
            "schedule" to newData.schedule,
            "playerNum" to newData.playerNum,
            "area" to newData.area,
            "gender" to newData.gender,
            "level" to newData.level,
            "pay" to newData.pay,
            "description" to newData.description,
            "postImg" to newData.postImg
        )
        val snapshot = query.get().await()
        if (snapshot.exists()) {
            for (childSnapshot in snapshot.children) {
                childSnapshot.ref.updateChildren(dataToUpdate as Map<String, Any>)
            }
        }
    }

    override suspend fun getApplicationList(userId: String, database: FirebaseDatabase): List<TeamItem.ApplicationItem> {
        val teamRef = database.getReference("Team")
        val items = arrayListOf<TeamItem.ApplicationItem>()
        val snapshot = teamRef.get().await()
        if (snapshot.exists()) {
            for (childSnapshot in snapshot.children) {
                val applicationData = childSnapshot.getValue(TeamItem.ApplicationItem::class.java)
                if (applicationData != null && applicationData.userId == userId && applicationData.type == "용병신청") {
                    items.add(applicationData)
                }
            }
        }
        return items
    }

    override suspend fun deleteApplicationData(data: TeamItem.ApplicationItem, database: FirebaseDatabase) {
        val teamRef = database.getReference("Team")
        val query = teamRef.orderByChild("teamId").equalTo(data.teamId)
        val snapshot = query.get().await()
        if (snapshot.exists()) {
            for (childSnapshot in snapshot.children) {
                childSnapshot.ref.removeValue()
            }
        }
    }

    override suspend fun editApplicationData(data: TeamItem.ApplicationItem, newData: TeamItem.ApplicationItem, database: FirebaseDatabase) {
        val teamRef = database.getReference("Team")
        val query = teamRef.orderByChild("teamId").equalTo(data.teamId)

        val dataToUpdate = hashMapOf(
            "game" to newData.game,
            "schedule" to newData.schedule,
            "playerNum" to newData.playerNum,
            "area" to newData.area,
            "gender" to newData.gender,
            "level" to newData.level,
            "description" to newData.description,
            "postImg" to newData.postImg,
            "age" to newData.age,
        )
        val snapshot = query.get().await()
        if (snapshot.exists()) {
            for (childSnapshot in snapshot.children) {
                childSnapshot.ref.updateChildren(dataToUpdate as Map<String, Any>)
            }
        }
    }
}