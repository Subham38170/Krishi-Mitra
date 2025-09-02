package com.example.krishimitra.domain.crops_model

data class CropModel(
    val cropName: String ="",
    val state: String = "",
    val district: String = "",
    val village: String = "",
    val time: Long= System.currentTimeMillis(),
    val quantity: Long = 0,
    val cropType: String = "",
    val price_per_unit: Double = 0.0,
    val mobileNo: String = "",
    val farmerId: String = "",
    val imageUrl: String? = ""
)
