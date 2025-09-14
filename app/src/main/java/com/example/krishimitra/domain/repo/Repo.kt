package com.example.krishimitra.domain.repo

import android.app.Activity
import androidx.paging.PagingData
import com.example.krishimitra.data.local.entity.MandiPriceEntity
import com.example.krishimitra.domain.ResultState
import com.example.krishimitra.domain.location_model.Location
import com.example.krishimitra.domain.mandi_data_models.MandiPriceDto
import kotlinx.coroutines.flow.Flow

interface Repo {


    suspend fun getLocation(): Location?

    fun getLanguage(): Flow<String>

    suspend fun changeLanguage(lang: String)

    fun requestLocationPermission(activity: Activity)


    suspend fun getMandiPrices(
        offset: Int? = null,
        limit: Int? = null,
        state: String? = null,
        district: String? = null,
        market: String? = null,
        commodity: String? = null,
        variety: String? = null,
        grade: String? = null
    ): Flow<ResultState<List<MandiPriceDto>>>


    fun getMandiPricesPaging(
        state: String? = null,
        district: String? = null,
        market: String? = null,
        commodity: String? = null,
        variety: String? = null,
        grade: String? = null
    ): Flow<PagingData<MandiPriceEntity>>
}
