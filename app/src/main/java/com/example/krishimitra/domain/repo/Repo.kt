package com.example.krishimitra.domain.repo

import android.app.Activity
import com.example.krishimitra.domain.location_model.Location
import kotlinx.coroutines.flow.Flow

interface Repo {


    suspend fun getLocation(): Location?

    fun getLanguage(): Flow<String>

    suspend fun changeLanguage(lang: String)

    fun requestLocationPermission(activity: Activity)
}
