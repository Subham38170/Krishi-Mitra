package com.example.krishimitra.presentation.home_screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.krishimitra.presentation.components.top_app_bars.HomeScreenTopBar

@Composable
fun HomeScreen(
    onEvent: (HomeScreenEvent)-> Unit,
    state: HomeScreenState
) {

    Scaffold(
        topBar = {
            HomeScreenTopBar(
                currentLanguage = state.currentLanguage,
                onLanguageChange = {
                    onEvent(HomeScreenEvent.ChangeLanguage(it))
                }
            )
        },

        ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

        }

    }
}