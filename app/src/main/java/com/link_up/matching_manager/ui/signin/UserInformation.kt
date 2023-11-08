package com.link_up.matching_manager.ui.signin

import com.link_up.matching_manager.data.model.UserInfoModel

object UserInformation {
    var userInfo: com.link_up.matching_manager.data.model.UserInfoModel =
        com.link_up.matching_manager.data.model.UserInfoModel(
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
