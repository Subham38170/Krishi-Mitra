package com.example.krishimitra.presentation.buy_sell_screen

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import com.example.krishimitra.domain.crops_model.CropModel

@Composable
fun SellScreen(
    cropLists: List<CropModel>
) {

    LazyColumn(

    ) {
        items(cropLists) {

        }


    }

}