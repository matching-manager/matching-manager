package com.link_up.matching_manager.data.repository.database

import android.util.Log
import com.link_up.matching_manager.data.model.UserInfoModel
import com.link_up.matching_manager.domain.repository.database.UserInfoRepository
import com.link_up.matching_manager.ui.signin.UserInformation
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await

class UserInfoRepositoryImpl : UserInfoRepository {

    private val TAG = "UserInfoRepositoryImpl"

    override suspend fun checkUser(data: com.link_up.matching_manager.data.model.UserInfoModel, database: FirebaseDatabase): Boolean {
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
                        "fcmToken" to data.fcmToken
                    )
                    // 중복된 데이터일 경우 Fcm 토큰만 업데이트
                    userRef.child(key).updateChildren(updateData).await()

                    // 중복된 데이터를 가져와서 userInfo에 할당
                    val model = childSnapshot.getValue(com.link_up.matching_manager.data.model.UserInfoModel::class.java)
                    Log.d(TAG, "getModel : $model")

                    val updatedUserInfo = com.link_up.matching_manager.data.model.UserInfoModel(
                        uid = model?.uid,
                        uidToken = model?.uidToken,
                        fcmToken = model?.fcmToken,
                        email = model?.email,
                        photoUrl = model?.photoUrl,
                        username = model?.username,
                        phoneNumber = model?.phoneNumber,
                        realName = model?.realName,
                    )
                    UserInformation.userInfo = updatedUserInfo
                }
            }
        } else {
            // 중복된 정보가 없는 경우 여기까지의 정보를 userInfo에 할당
            UserInformation.userInfo = data
            // 중복 정보가 없는 경우, New User 라고 판단 -> false return
            return false
        }
        // 중복된 데이터가 있을 경우 Existing User 라고 판단 -> true return
        return true
    }

    override suspend fun adduser(data: com.link_up.matching_manager.data.model.UserInfoModel, database: FirebaseDatabase) {
        val userRef = database.getReference("User")
        userRef.push().setValue(data).await()
    }


//    override suspend fun adduser(data: UserInfoModel, database: FirebaseDatabase){
//        val userRef = database.getReference("User")
//
//        // 중복 정보를 확인할 쿼리 생성
//        val query = userRef.orderByChild("uid").equalTo(data.uid)
//
//        // 쿼리 실행
//        val snapshot = query.get().await()
//
//        if (snapshot.exists()) {
//            // 중복 정보가 이미 데이터베이스에 있는 경우, 업데이트 수행
//            for (childSnapshot in snapshot.children) {
//                val key = childSnapshot.key
//                key?.let {
//                    val updateData = mapOf(
//                        "uid" to data.uid,
//                        "uidToken" to data.uidToken,
//                        "email" to data.email,
//                        "fcmToken" to data.fcmToken,
//                        "photoUrl" to data.photoUrl
//                    )
//                    // 중복된 데이터 업데이트
//                    userRef.child(key).updateChildren(updateData).await()
//                }
//            }
//        } else {
//            // 중복 정보가 없는 경우, 데이터를 저장
//            userRef.push().setValue(data).await()
//        }
//    }

}
