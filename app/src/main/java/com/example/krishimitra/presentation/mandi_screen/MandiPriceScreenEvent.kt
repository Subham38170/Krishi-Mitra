package com.example.krishimitra.presentation.mandi_screen

sealed class MandiPriceScreenEvent {

    data class onStateSelect(val state: String): MandiPriceScreenEvent()


    data class onDistrictSelect(val district: String): MandiPriceScreenEvent()


    data object onDistrictDeselect: MandiPriceScreenEvent()

    data object onStateDeselect: MandiPriceScreenEvent()


    data class onSearch(val searchText: String): MandiPriceScreenEvent()

    data object loadAllCrops: MandiPriceScreenEvent()
}