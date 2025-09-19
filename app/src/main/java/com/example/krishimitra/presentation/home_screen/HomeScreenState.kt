package com.example.krishimitra.presentation.home_screen

import com.example.krishimitra.domain.govt_scheme_slider.BannerModel

data class HomeScreenState(
    val currentLanguage: String = "English",
    val schemeBannersList: List<BannerModel> = emptyList(),
    val isBannersLoading: Boolean = false,
    val isKrishiNewsLoading: Boolean = false,
    val krishiNewsBannerList: List<BannerModel> = emptyList()
)