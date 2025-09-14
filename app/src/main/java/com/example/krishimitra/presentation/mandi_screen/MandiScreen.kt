package com.example.krishimitra.presentation.mandi_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.LoadType
import androidx.paging.compose.LazyPagingItems
import com.example.krishimitra.data.local.entity.MandiPriceEntity
import com.example.krishimitra.data.mappers.toDto
import com.example.krishimitra.presentation.components.MandiPriceItem
import kotlinx.coroutines.flow.StateFlow

@Composable
fun MandiScreen(
    state: MandiScreenState,
    mandiPrice:  LazyPagingItems<MandiPriceEntity>
) {


    val windowInfo = LocalWindowInfo.current
    val containerWidth = windowInfo.containerSize.width
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center,

        ) {

        if(mandiPrice.loadState.refresh is LoadState.Loading) CircularProgressIndicator()
        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(mandiPrice.itemCount) {index->
               mandiPrice[index]?.let {
                   MandiPriceItem(
                       mandiPrice = it.toDto(),
                       imageSize = if (containerWidth > 600) 100.dp else 80.dp
                   )
               }
            }
            item {
                if(mandiPrice.loadState.append is LoadState.Loading){
                    CircularProgressIndicator()
                }
            }
        }
    }
}