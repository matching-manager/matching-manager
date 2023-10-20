package com.example.matching_manager.fcm


data class Payload(
    val message : PayloadMessage
)
data class PayloadMessage(
    val token : String,
    val notification : PayloadNotification
)
data class  PayloadNotification(
    val body : String,
    val title: String,
    val userId: String,
    val phoneNumber: String
)