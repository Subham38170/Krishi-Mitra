package com.example.krishimitra.presentation.disease_prediction_screen

import android.net.Uri
import com.example.krishimitra.domain.disease_prediction_model.Prediction

sealed class DiseasePredictionScreenEvent {

    data class PredictCropDisease(val imageUrl: Uri): DiseasePredictionScreenEvent()

    data class onSpeak(val predictedData: Prediction): DiseasePredictionScreenEvent()

    data object onStopSpeak: DiseasePredictionScreenEvent()
}