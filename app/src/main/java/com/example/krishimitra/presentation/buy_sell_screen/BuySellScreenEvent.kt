package com.example.krishimitra.presentation.buy_sell_screen

sealed class BuySellScreenEvent {

    data class onSearch(val searching: String): BuySellScreenEvent()
    data class onSuggestionSearch(val suggestion: String): BuySellScreenEvent()
}