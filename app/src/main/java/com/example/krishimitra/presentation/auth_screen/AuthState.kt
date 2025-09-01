package com.example.krishimitra.presentation.auth_screen

import com.example.krishimitra.Language
import com.example.krishimitra.domain.location_model.Location

data class AuthState(
    val isError: String? = null,
    val isSuccess: Boolean = false,
    val isSignLoading: Boolean = false,
    val currentLanguage: String = "English",
    val location: Location? = null,
    val isLocationLoading: Boolean = false
)
