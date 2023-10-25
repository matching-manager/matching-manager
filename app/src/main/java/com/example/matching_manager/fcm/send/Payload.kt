package com.example.matching_manager.fcm.send

import com.google.gson.annotations.SerializedName

data class Payload(val registration_id: String, val id: String, val number: String) {

    val to = registration_id
    val data: Data = Data(userId = id, phoneNumber = number)
    var notification: Notification = Notification()

    @Suppress("PropertyName")
    data class Notification(
        var body: String = "notification Body test",
        var click_action: String = "click",
        var tag: String = "tag",
        var title: String = "notification title test",
        var icon: String = "icon"
    )

    data class Data(
        @SerializedName("user_id")
        var userId: String,
        @SerializedName("phone_number")
        var phoneNumber: String,
    )
}
