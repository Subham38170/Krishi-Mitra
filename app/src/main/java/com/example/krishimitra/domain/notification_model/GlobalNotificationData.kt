package com.example.krishimitra.domain.notification_model

data class GlobalNotificationData(
    val title: String = "",
    val description: String = "",
    val imageUrl: String? = null,
    val webLink: String? = null,
    val timeStamp: Long = System.currentTimeMillis()
)