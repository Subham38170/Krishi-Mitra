package com.example.krishimitra.domain.weather_models

data class Hourly(
    val condition: String = "",
    val feels_like: Int = 0,
    val temperature: Int = 0,
    val time: String = "",
)
