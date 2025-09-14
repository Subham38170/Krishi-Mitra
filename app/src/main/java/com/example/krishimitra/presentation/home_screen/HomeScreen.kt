package com.example.krishimitra.presentation.home_screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.example.krishimitra.R
import com.example.krishimitra.presentation.components.top_app_bars.HomeScreenTopBar

@Composable
fun HomeScreen(
    onEvent: (HomeScreenEvent) -> Unit,
    state: HomeScreenState,
    moveToMandiScreen: () -> Unit,
    moveToDiseasePredictionScreen: () -> Unit
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
                .padding(8.dp)
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                ElevatedButton(
                    onClick = moveToMandiScreen,
                    modifier = Modifier
                        .weight(1f)
                        .height(160.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(id = R.color.grass_green)
                    )

                ) {
                    Text(
                        text = "Mandi Price",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                ElevatedButton(
                    onClick = moveToDiseasePredictionScreen,
                    modifier = Modifier
                        .weight(1f)
                        .height(160.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(id = R.color.grass_green)
                    )

                ) {
                    Text(
                        text = "Disease Detection",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }

            }
        }

    }
}