package com.example.krishimitra.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.krishimitra.data.local.type_converters.HourlyListConverter
import com.example.krishimitra.domain.weather_models.Hourly


@Entity(tableName = "weather_data")
data class WeatherEntity(
    @PrimaryKey val date: String,
    val max_temp: Int,
    val min_temp: Int,
    @TypeConverters(HourlyListConverter::class)
    val hourly: List<Hourly>
)

