package com.example.krishimitra.presentation.buy_sell_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.krishimitra.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BuySellScreen(
    state: BuySellScreenState, onEvent: (BuySellScreenEvent) -> Unit
) {


    var screen by remember { mutableStateOf("buy") }

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

    val pagerState = rememberPagerState { 2 }
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = colorResource(id = R.color.light_green)
                ), title = {
                    Box(
                        modifier = Modifier
                            .width(200.dp)
                            .height(36.dp)
                            .border(
                                color = colorResource(id = R.color.slight_dark_green),
                                shape = RoundedCornerShape(40.dp),
                                width = 1.dp
                            )
                            .background(Color.White, shape = RoundedCornerShape(40.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .width(110.dp)
                                .height(36.dp)
                                .background( colorResource(id = R.color.slight_dark_green), shape = RoundedCornerShape(40.dp))
                                .align(if(screen=="buy") Alignment.TopStart else Alignment.TopEnd)
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ){
                            Text(
                                text = "Buy",
                                color = if(screen=="sell") colorResource(id = R.color.slight_dark_green) else Color.White,
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.clickable(
                                    enabled = true,
                                    interactionSource = remember { MutableInteractionSource() },
                                    onClick = {
                                        screen = "buy"
                                    },
                                    indication = null
                                )

                            )

                            Text(
                                text = "Sell",
                                color = if(screen=="buy") colorResource(id = R.color.slight_dark_green) else Color.White,
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.clickable(
                                    enabled = true,
                                    interactionSource = remember { MutableInteractionSource() },
                                    onClick = {
                                        screen = "sell"
                                    },
                                    indication = null
                                )
                            )


                        }




                    }
                })
        }) {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .background(colorResource(id = R.color.light_green))
        ) {

            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxSize()
            ) {
                when(it){
                    0 ->{
                        BuyScreen()
                    }
                    1 ->{

                        SellScreen(emptyList())
                    }
                }
            }
        }
    }
}