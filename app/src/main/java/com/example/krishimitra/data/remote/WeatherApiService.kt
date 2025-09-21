package com.example.krishimitra.data.remote

import com.example.krishimitra.Constants
import com.example.krishimitra.domain.weather_models.WeatherApiResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface WeatherApiService {

    @GET("global/forecast")
    suspend fun getForecast(
        @Query("location") location: String,
        @Header("X-Api-Key") apiKey: String = Constants.WEATHER_API_KEY
    ): Response<WeatherApiResponse>

}