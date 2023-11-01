package com.example.matching_manager.data.repository.database

import com.example.matching_manager.data.model.UserInfoModel
import com.example.matching_manager.domain.repository.database.UserInfoRepository
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await

class UserInfoRepositoryImpl : UserInfoRepository {

    override suspend fun adduser(data: UserInfoModel, database: FirebaseDatabase) {
        val userRef = database.getReference("User")

        // 중복 정보를 확인할 쿼리 생성
        val query = userRef.orderByChild("uid").equalTo(data.uid)

        // 쿼리 실행
        val snapshot = query.get().await()

        if (snapshot.exists()) {
            // 중복 정보가 이미 데이터베이스에 있는 경우, 업데이트 수행
            for (childSnapshot in snapshot.children) {
                val key = childSnapshot.key
                key?.let {
                    val updateData = mapOf(
                        "uid" to data.uid,
                        "uidToken" to data.uidToken,
                        "email" to data.email,
                        "fcmToken" to data.fcmToken,
                        "photoUrl" to data.photoUrl
                    )
                    // 중복된 데이터 업데이트
                    userRef.child(key).updateChildren(updateData).await()
                }
            }
        } else {
            // 중복 정보가 없는 경우, 데이터를 저장
            userRef.push().setValue(data).await()
        }
    }

}
