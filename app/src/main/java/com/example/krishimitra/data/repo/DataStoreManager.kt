package com.example.krishimitra.data.repo

import android.content.Context
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.krishimitra.Constants.dataStore
import com.example.krishimitra.Constants.userDataStore
import com.example.krishimitra.domain.farmer_data.UserDataModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class DataStoreManager @Inject constructor(
    private val context: Context
) {
    companion object {
        private val USER_NAME = stringPreferencesKey("user_name")
        private val USER_STATE = stringPreferencesKey("user_state")
        private val USER_MOBILE = stringPreferencesKey("user_mobile")
        private val USER_DISTRICT = stringPreferencesKey("user_district")
        private val USER_EMAIL = stringPreferencesKey("user_email")
        private val USER_VILLAGE = stringPreferencesKey("user_village")
        private val USER_PINCODE = stringPreferencesKey("user_pincode")
        private val USER_LATITUDE = doublePreferencesKey("user_pincode")
        private val USER_LONGITUDE = doublePreferencesKey("user_pincode")


    }

    suspend fun storeUserData(
        userData: UserDataModel
    ) {
        context.dataStore.edit {

        }
    }

    fun getUser(): Flow<UserDataModel> {
        return context.userDataStore.data.map { prefs ->
            UserDataModel(
                name = prefs[USER_NAME] ?: "",
                email = prefs[USER_EMAIL] ?: "",
                village = prefs[USER_VILLAGE] ?: "",
                district = prefs[USER_DISTRICT] ?: "",
                state = prefs[USER_STATE] ?: "",
                mobileNo = prefs[USER_MOBILE] ?: "",
                pinCode = prefs[USER_PINCODE] ?: "",
                latitude = prefs[USER_LATITUDE] ?: 28.6139,
                longitude = prefs[USER_LONGITUDE] ?: 77.2090
            )
        }
    }

    fun getUserName(): Flow<String> {
        return context.userDataStore.data.map { prefs ->
            prefs[USER_NAME] ?: ""
        }
    }

    fun getStateName(): Flow<String> {
        return context.userDataStore.data.map { prefs ->
            prefs[USER_STATE] ?: ""
        }
    }
    fun getDistrictName(): Flow<String> {
        return context.userDataStore.data.map { prefs ->
            prefs[USER_DISTRICT] ?: ""
        }
    }
    fun getVillageName(): Flow<String> {
        return context.userDataStore.data.map { prefs ->
            prefs[USER_VILLAGE] ?: ""
        }
    }

    fun getLatAndLang(): Flow<List<Double>> {
        return context.userDataStore.data.map { prefs ->
            listOf(prefs[USER_LATITUDE] ?: 28.6139, prefs[USER_LONGITUDE] ?: 77.2090)
        }
    }
    fun getMobileNo(): Flow<String> {
        return context.userDataStore.data.map { prefs ->
            prefs[USER_MOBILE] ?: ""
        }
    }
}