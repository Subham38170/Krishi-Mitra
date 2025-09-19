package com.example.krishimitra.domain.disease_prediction_model

data class DiseasePredictionResponse(
    val predictions: List<Prediction> = listOf()
)


data class Prediction(
    val Description: String = "",
    val Disease: String = "",
    val Precautions: List<String> = listOf(),
    val Treatment: List<String> = listOf(),
    val confidence: Double = 0.0
)