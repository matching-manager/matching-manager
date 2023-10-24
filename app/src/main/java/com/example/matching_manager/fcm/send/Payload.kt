package com.example.matching_manager.fcm.send
import com.google.gson.annotations.SerializedName

data class Payload(val registration_id: String) {

    val to = registration_id
    val data: Data = Data()
    var notification: Notification = Notification()

    @Suppress("PropertyName")
    data class Notification(
        var body: String = "test Body",
        var click_action: String = "click",
        var tag: String = "tag",
        var title: String = "title message",
        var icon: String = "icon"
    )

    data class Data(
        @SerializedName("user_id")
        var userId: String = "softychoo",
        @SerializedName("phone_number")
        var phoneNumber: String = "01028179282",
        var title: String = "Fcm message",
    )
}
