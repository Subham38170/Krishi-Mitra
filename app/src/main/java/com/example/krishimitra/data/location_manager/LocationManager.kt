package com.example.krishimitra.data.location_manager

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.IntentSender
import android.location.Geocoder
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresPermission
import com.example.krishimitra.domain.location_model.Location
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.Priority
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class LocationManager @Inject
constructor(
    private val context: Context,
    private val fusedLocationClient: FusedLocationProviderClient,

    ) {
    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    suspend fun getLocation(): Location? {
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
                        geocoder.getFromLocation(
                            location.latitude,
                            location.longitude,
                            1
                        ) { addresses ->
                            if (addresses.isNotEmpty()) {
                                val address = addresses[0]
                                cont.resume(
                                    Location(
                                        village = address.subLocality ?: address.locality
                                        ?: address.featureName ?: "",
                                        state = address.adminArea ?: "",
                                        district = address.subAdminArea ?: address.locality ?: "",
                                        pinCode = address.postalCode ?: ""
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
                        val addresses =
                            geocoder.getFromLocation(location.latitude, location.longitude, 1)
                        if (!addresses.isNullOrEmpty()) {
                            val address = addresses[0]
                            cont.resume(
                                Location(
                                    village = address.subLocality ?: "",
                                    state = address.adminArea ?: "",
                                    district = address.subAdminArea ?: "",
                                    pinCode = address.postalCode ?: ""
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

    companion object {
        fun isLocationEnabled(context: Context): Boolean {
            val locationserviceManager =
                context.getSystemService(Context.LOCATION_SERVICE) as android.location.LocationManager
            return locationserviceManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER) ||
                    locationserviceManager.isProviderEnabled(android.location.LocationManager.NETWORK_PROVIDER)
        }
    }

    fun requestEnableLocation(activity: Activity) {
        val locationRequest = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY,
            10000L // update interval in ms
        ).setMinUpdateIntervalMillis(5000L) // fastest interval
            .build()

        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
            .setAlwaysShow(true) // always show the system dialog if needed

        val client = LocationServices.getSettingsClient(activity)
        val task = client.checkLocationSettings(builder.build())

        task.addOnSuccessListener {
            Log.d("Location", "Location already enabled")
        }

        task.addOnFailureListener { exception ->
            if (exception is ResolvableApiException) {
                try {
                    exception.startResolutionForResult(activity, 1001)
                } catch (_: IntentSender.SendIntentException) {
                }
            }
        }
    }


}