package com.example.krishimitra.domain.repo

import com.example.krishimitra.domain.location_model.Location
import kotlinx.coroutines.flow.Flow

interface Repo {


    suspend fun getLocation(): Location?

    fun getLanguage(): Flow<String>

    suspend fun changeLanguage(lang: String)
}