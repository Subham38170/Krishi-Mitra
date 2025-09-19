package com.example.krishimitra.presentation.disease_prediction_screen

import com.example.krishimitra.domain.disease_prediction_model.DiseasePredictionResponse

data class DiseasePredictionScreenState(
    val isLoading: Boolean = false,
    val response: DiseasePredictionResponse? = null

)