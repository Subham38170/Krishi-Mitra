package com.example.krishimitra.presentation.mandi_screen

import com.example.krishimitra.domain.mandi_data_models.MandiPriceDto

data class MandiScreenState(
    val error: String? = null,
    val isLoading: Boolean = false,
    val mandiPriceList: List<MandiPriceDto> = emptyList()
)