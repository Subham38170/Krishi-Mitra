package com.example.krishimitra.data.repo

import android.Manifest
import android.app.Activity
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.annotation.RequiresPermission
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.krishimitra.Constants
import com.example.krishimitra.FirebaseConstants
import com.example.krishimitra.data.local.KrishiMitraDatabase
import com.example.krishimitra.data.local.entity.MandiPriceEntity
import com.example.krishimitra.data.remote.CropDiseasePredictionApiService
import com.example.krishimitra.data.remote.MandiPriceApiService
import com.example.krishimitra.data.remote_meidator.MandiPriceRemoteMediator
import com.example.krishimitra.data.repo.lang_manager.LanguageManager
import com.example.krishimitra.data.repo.location_manager.LocationManager
import com.example.krishimitra.domain.ResultState
import com.example.krishimitra.domain.disease_prediction_model.DiseasePredictionResponse
import com.example.krishimitra.domain.farmer_data.UserDataModel
import com.example.krishimitra.domain.govt_scheme_slider.BannerModel
import com.example.krishimitra.domain.location_model.Location
import com.example.krishimitra.domain.mandi_data_models.MandiPriceDto
import com.example.krishimitra.domain.repo.Repo
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class RepoImpl @Inject constructor(
    private val firestoreDb: FirebaseFirestore,
    private val languageManager: LanguageManager,
    private val locationManager: LocationManager,
    private val mandiApiService: MandiPriceApiService,
    private val diseasePredictionApiService: CropDiseasePredictionApiService,
    private val localDb: KrishiMitraDatabase,
    private val dataStoreManager: DataStoreManager,
    private val context: Context
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

    override suspend fun storeUserData(userData: UserDataModel) {
        dataStoreManager.storeUserData(userData)
    }

    override fun getUserData(): Flow<UserDataModel> {
        return dataStoreManager.getUser()
    }

    override fun getUserName(): Flow<String> {
        return dataStoreManager.getUserName()
    }

    override fun getStateName(): Flow<String> {
        return dataStoreManager.getStateName()
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

    override fun loadGovtSchemes(): Flow<ResultState<List<BannerModel>>> {
        return callbackFlow {
            trySend(ResultState.Loading)
            firestoreDb.collection(FirebaseConstants.GOVT_SCHEMES)
                .addSnapshotListener { snapshot, e ->
                    if (e != null) {
                        trySend(ResultState.Error(e.message.toString()))
                        return@addSnapshotListener
                    }
                    snapshot?.let {
                        val schemes = it.toObjects(BannerModel::class.java)
                        trySend(ResultState.Success(schemes))
                    }
                }
            awaitClose { close() }
        }

    }


    override  fun predictCropDisease(lang: String,filePath: Uri): Flow<ResultState<DiseasePredictionResponse>> {
        return callbackFlow {
            trySend(ResultState.Loading)
            try {
                val imagePart = uriToMultipart(context, filePath)
                val response = diseasePredictionApiService.uploadImage(lang,imagePart)
                if (response.isSuccessful) {
                    val result = response.body()
                    result?.let {
                        trySend(ResultState.Success(result))
                    }
                } else {
                    trySend(ResultState.Error(response.message()))
                }
            } catch (e: Exception) {
                trySend(ResultState.Error(e.message.toString()))
            }
            awaitClose { close() }
        }
    }

    override suspend fun loadKrishiNews(): Flow<ResultState<List<BannerModel>>> {
        return callbackFlow {
            trySend(ResultState.Loading)
            firestoreDb.collection(FirebaseConstants.KRISHI_NEWS)
                .addSnapshotListener { snapshot, e ->
                    if (e != null) {
                        trySend(ResultState.Error(e.message.toString()))
                        return@addSnapshotListener
                    }
                    snapshot?.let {
                        val news = it.toObjects(BannerModel::class.java)
                        trySend(ResultState.Success(news))
                    }
                }
            awaitClose { close() }
        }    }


    fun uriToMultipart(context: Context, uri: Uri): MultipartBody.Part {
        val inputStream = context.contentResolver.openInputStream(uri)!!
        val fileBytes = inputStream.readBytes()
        val requestBody = RequestBody.create("image/jpeg".toMediaTypeOrNull(), fileBytes)
        val fileName = "image.jpg" // you can extract name from uri if needed
        return MultipartBody.Part.createFormData("file", fileName, requestBody)
    }







}