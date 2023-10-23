package com.example.matching_manager.fcm.send

import com.google.gson.annotations.SerializedName

//data class Payload(
//    val message: PayloadMessage
//)
//
//data class PayloadMessage(
//    val token: String,
//    val notification: PayloadNotification
//)
//
//data class PayloadNotification(
//    val body: String,
//    val title: String,
//    val userId: String,
//    val phoneNumber: String
//)

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
        var phone_number: String = "01028179282",
        var title: String = "Fcm message",
        var body: String = "Fcm body",
    )
}


//{ "notification": {
//    "body": "This is an FCM notification message!",
//    "time": "FCM Message"
//},
//    "to" : "bk3RNwTe3H0:CI2k_HHwgIpoDKCIZvvDMExUdFQ3P1..."
//}

//{
//    "message":{
//    "token":"bk3RNwTe3H0:CI2k_HHwgIpoDKCIZvvDMExUdFQ3P1...",
//    "notification":{
//        "body":"This is an FCM notification message!",
//        "title":"FCM Message"
//    }
//}
//}