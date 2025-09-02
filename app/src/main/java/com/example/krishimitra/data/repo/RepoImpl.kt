package com.example.krishimitra.data.repo

import android.Manifest
import android.app.Activity
import android.content.Context
import android.location.Geocoder
import android.os.Build
import androidx.annotation.RequiresPermission
import com.example.krishimitra.data.lang_manager.LanguageManager
import com.example.krishimitra.data.location_manager.LocationManager
import com.example.krishimitra.domain.location_model.Location
import com.example.krishimitra.domain.repo.Repo
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class RepoImpl @Inject constructor(
    private val languageManager: LanguageManager,
    private val locationManager: LocationManager
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


}