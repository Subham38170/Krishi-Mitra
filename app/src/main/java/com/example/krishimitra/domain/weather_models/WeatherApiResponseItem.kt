package com.example.krishimitra.domain.weather_models

data class WeatherApiResponseItem(
    val date: String = "",
    val hourly: List<Hourly> = listOf(),
    val max_temp: Int = 0,
    val min_temp: Int = 0,

)