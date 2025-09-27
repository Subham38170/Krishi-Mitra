package com.example.krishimitra.domain.feedback_model

data class FeedbackData(
    val uid: String = "",
    val title: String = "",
    val description: String = "",
    val improvement: String = "",
    val issues: String = "",
    val userRating: Int = -1,
    val language: String = "hi-In"
)
