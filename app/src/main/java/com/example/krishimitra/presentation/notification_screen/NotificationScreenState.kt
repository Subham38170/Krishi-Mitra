package com.example.krishimitra.presentation.notification_screen

import com.example.krishimitra.domain.notification_model.GlobalNotificationData

data class NotificationScreenState(
    val isLoading: Boolean = false,
    val notificationList: List<GlobalNotificationData> = emptyList()
)