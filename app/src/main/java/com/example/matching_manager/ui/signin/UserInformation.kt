package com.example.matching_manager.ui.signin

import com.example.matching_manager.data.model.UserInfoModel
import com.google.firebase.firestore.auth.User

object UserInformation {
    var userInfo: UserInfoModel = UserInfoModel(
        uid = null,
        uidToken = null,
        email = null,
        fcmToken = null,
        photoUrl = null,
        username = null,
        phoneNUmber = null
    )
}
