package com.example.krishimitra.data.repo

import android.Manifest
import android.app.Activity
import android.util.Log
import androidx.annotation.RequiresPermission
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.krishimitra.data.local.KrishiMitraDatabase
import com.example.krishimitra.data.local.entity.MandiPriceEntity
import com.example.krishimitra.data.remote.MandiPriceApiService
import com.example.krishimitra.data.remote_meidator.MandiPriceRemoteMediator
import com.example.krishimitra.data.repo.lang_manager.LanguageManager
import com.example.krishimitra.data.repo.location_manager.LocationManager
import com.example.krishimitra.domain.ResultState
import com.example.krishimitra.domain.location_model.Location
import com.example.krishimitra.domain.mandi_data_models.MandiPriceDto
import com.example.krishimitra.domain.repo.Repo
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class RepoImpl @Inject constructor(
    private val languageManager: LanguageManager,
    private val locationManager: LocationManager,
    private val mandiApiService: MandiPriceApiService,
    private val localDb: KrishiMitraDatabase
) : Repo {
    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    override suspend fun getLocation(): Location? {
        return locationManager.getLocation()
    }


    override fun getLanguage(): Flow<String> {
        return languageManager.getLanguage()
    }

    override suspend fun changeLanguage(lang: String) {
        languageManager.updateLanguage(lang)
    }

    override fun requestLocationPermission(activity: Activity) {
        locationManager.requestEnableLocation(activity = activity)
    }

    override suspend fun getMandiPrices(
        offset: Int?,
        limit: Int?,
        state: String?,
        district: String?,
        market: String?,
        commodity: String?,
        variety: String?,
        grade: String?
    ): Flow<ResultState<List<MandiPriceDto>>> {
        return callbackFlow {
            trySend(ResultState.Loading)
            try {
                val response = mandiApiService.getMandiPrices(
                    offset = offset,
                    limit = limit,
                    state = state,
                    district = district,
                    market = market,
                    commodity = commodity,
                    variety = variety,
                    grade = grade
                )
                Log.d("API_DATA", response.body()?.records.toString())
                if (response.isSuccessful) {
                    trySend(ResultState.Success(response.body()?.records ?: emptyList()))
                } else {
                    trySend(ResultState.Error("Something went wrong? ${response.message()}"))
                }
            } catch (e: Exception) {
                trySend(ResultState.Error(e.message.toString()))
            }

            awaitClose { close() }
        }
    }

    @OptIn(ExperimentalPagingApi::class)
    override fun getMandiPricesPaging(
        state: String?,
        district: String?,
        market: String?,
        commodity: String?,
        variety: String?,
        grade: String?
    ): Flow<PagingData<MandiPriceEntity>> {
        val pagingSourceFactory = { localDb.mandiPriceDao().getAllMandiPrices() }
        return Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false),
            remoteMediator = MandiPriceRemoteMediator(
                localDb = localDb,
                mandiPriceApi = mandiApiService,
                stateFilter = state,
                districtFilter = district,
                marketFilter = market,
                varietyFilter = variety,
                commodityFilter = commodity
            ),
            pagingSourceFactory = pagingSourceFactory

        ).flow
    }


}