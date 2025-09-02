package com.example.krishimitra.presentation.buy_sell_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.krishimitra.R
import kotlin.math.exp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BuySellScreen(
    state: BuySellScreenState,
    onEvent: (BuySellScreenEvent) -> Unit
) {


    val cropList = listOf(
        stringResource(R.string.vegetables),
        stringResource(R.string.fruits),
        stringResource(R.string.grains),
        stringResource(R.string.pulses),
        stringResource(R.string.oilseeds),
        stringResource(R.string.spices),
        stringResource(R.string.cash_crops),
        stringResource(R.string.plantation_crops),
        stringResource(R.string.fodder_crops)
    )
    Scaffold(
        topBar = {

            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.krishi_bazar),
                        style = MaterialTheme.typography.headlineLarge
                    )
                }
            )

        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ){
            
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ){
                items(cropList){
                    SuggestionChip(
                        onClick = {onEvent(BuySellScreenEvent.onSearch(it))},
                        label = {
                            Text(
                                text = it
                            )
                        }
                    )
                }
            }
            LazyColumn {

            }
        }
    }
}