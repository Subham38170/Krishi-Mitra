package com.example.krishimitra.presentation.feedback_screen

import com.example.krishimitra.domain.feedback_model.FeedbackData

sealed class FeedbackScreenEvent {

    data class sendFeedback(
        val feedbackData: FeedbackData
    ): FeedbackScreenEvent()
}