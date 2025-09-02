package com.example.krishimitra.presentation.buy_sell_screen

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject



@HiltViewModel
class BuySellScreenViewModel @Inject
constructor() : ViewModel() {

    private val _state = MutableStateFlow<BuySellScreenState>(BuySellScreenState())
    val state = _state.asStateFlow()


    fun onEvent(event: BuySellScreenEvent) {
        when (event) {
            is BuySellScreenEvent.onSearch -> {

            }

            is BuySellScreenEvent.onSuggestionSearch -> {

            }
        }
    }
}