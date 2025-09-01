package com.example.krishimitra.data.repo

import android.Manifest
import android.content.Context
import android.location.Geocoder
import android.os.Build
import androidx.annotation.RequiresPermission
import com.example.krishimitra.data.lang_manager.LanguageManager
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
    private val fusedLocationClient: FusedLocationProviderClient,
    private val context: Context
) : Repo {
    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    override suspend fun getLocation(): Location? {
        val geocoder = Geocoder(context)

        return suspendCancellableCoroutine { cont ->
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location == null) {
                    cont.resume(null)
                    return@addOnSuccessListener
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    // API 33+: async callback
                    try {
                        geocoder.getFromLocation(location.latitude, location.longitude, 1) { addresses ->
                            if (addresses.isNotEmpty()) {
                                val address = addresses[0]
                                cont.resume(
                                    Location(
                                        village = address.subLocality ?: address.locality ?: address.featureName ?: "",
                                        state = address.adminArea?:"",
                                        district =  address.subAdminArea ?: address.locality ?: "",
                                        pinCode = address.postalCode?:""
                                    )
                                )
                            } else {
                                cont.resume(null)
                            }
                        }
                    } catch (e: Exception) {
                        cont.resumeWithException(e)
                    }
                } else {
                    // API < 33: synchronous, run on background thread
                    try {
                        val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                        if (!addresses.isNullOrEmpty()) {
                            val address = addresses[0]
                            cont.resume(
                                Location(
                                    village = address.subLocality?:"",
                                    state = address.adminArea?:"",
                                    district = address.subAdminArea?:"",
                                    pinCode = address.postalCode?:""
                                )
                            )
                        } else {
                            cont.resume(null)
                        }
                    } catch (e: Exception) {
                        cont.resumeWithException(e)
                    }
                }
            }.addOnFailureListener { e ->
                cont.resumeWithException(e)
            }
        }
    }


    override fun getLanguage(): Flow<String> {
        return languageManager.getLanguage()
    }

    override suspend fun changeLanguage(lang: String) {
        languageManager.updateLanguage(lang)
    }


}