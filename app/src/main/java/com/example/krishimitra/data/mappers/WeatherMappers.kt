package com.example.krishimitra.data.mappers

import com.example.krishimitra.data.local.entity.WeatherEntity
import com.example.krishimitra.domain.weather_models.WeatherApiResponseItem


fun WeatherApiResponseItem.toEntity(): WeatherEntity {
    return WeatherEntity(
        date = date,
        max_temp = max_temp,
        min_temp = min_temp,
        hourly = hourly
    )
}


fun WeatherEntity.toDomain(): WeatherApiResponseItem {
    return WeatherApiResponseItem(
        date = date,
        max_temp = max_temp,
        min_temp = min_temp,
        hourly = hourly
    )
}