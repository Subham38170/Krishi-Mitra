package com.example.krishimitra.data.remote_meidator

import com.example.krishimitra.data.local.dao.WeatherDao
import com.example.krishimitra.data.mappers.toDomain
import com.example.krishimitra.data.mappers.toEntity
import com.example.krishimitra.data.remote.WeatherApiService
import com.example.krishimitra.domain.ResultState
import com.example.krishimitra.domain.repo.NetworkConnectivityObserver
import com.example.krishimitra.domain.repo.NetworkStatus
import com.example.krishimitra.domain.weather_models.WeatherApiResponseItem
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class WeatherRemoteMediator @Inject constructor(
    private val api: WeatherApiService,
    private val dao: WeatherDao,
    private val networkObserver: NetworkConnectivityObserver
) {

    @OptIn(ExperimentalCoroutinesApi::class)
    fun getWeather(location: String): Flow<ResultState<List<WeatherApiResponseItem>>> =
        networkObserver.networkStatus.flatMapLatest { status ->
            flow {
                emit(ResultState.Loading)
                val localData = dao.getALlWeatherData().map { it.toDomain() }

                if (status == NetworkStatus.Connected) {
                    try {
                        val remoteData = api.getForecast(location)
                        if (remoteData.isSuccessful) {
                            remoteData.body()?.let {
                                dao.updateWeatherTransaction(it.map { it.toEntity() })
                                emit(ResultState.Success(it))
                            } ?: run {
                                if (localData.isNotEmpty()) emit(ResultState.Success(localData))
                                else emit(ResultState.Error("Empty API response"))
                            }
                        } else {
                            if (localData.isNotEmpty()) emit(ResultState.Success(localData))
                            else emit(ResultState.Error(remoteData.message() ?: "API Error"))
                        }
                    } catch (e: Exception) {
                        if (localData.isNotEmpty()) emit(ResultState.Success(localData))
                        else emit(ResultState.Error(e.message ?: "Unknown error"))
                    }
                } else {
                    if (localData.isNotEmpty()) emit(ResultState.Success(localData))
                    else emit(ResultState.Error("No internet available"))
                }
            }
        }.catch { e ->
            emit(ResultState.Error(e.message ?: "Unexpected error"))
        }



}