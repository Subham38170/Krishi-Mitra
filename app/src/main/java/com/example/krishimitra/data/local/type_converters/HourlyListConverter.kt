package com.example.krishimitra.data.local.type_converters

import androidx.room.TypeConverter
import com.example.krishimitra.domain.weather_models.Hourly
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class HourlyListConverter {

    private val gson = Gson()

    @TypeConverter
    fun fromHourlyList(hourly: List<Hourly>): String {
        return gson.toJson(hourly)
    }

    @TypeConverter
    fun toHourlyList(data: String): List<Hourly> {
        val listType = object : TypeToken<List<Hourly>>() {}.type
        return gson.fromJson(data, listType)
    }
}