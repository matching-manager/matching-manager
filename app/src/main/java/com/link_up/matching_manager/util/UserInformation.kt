package com.link_up.matching_manager.util

import com.link_up.matching_manager.data.model.UserInfoModel

object UserInformation {
    var userInfo: UserInfoModel =
        UserInfoModel(
            uid = null,
            uidToken = null,
            email = null,
            fcmToken = null,
            photoUrl = null,
            username = null,
            phoneNumber = null,
            realName = null
        )
}
