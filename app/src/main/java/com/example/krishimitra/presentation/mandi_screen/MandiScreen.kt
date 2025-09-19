package com.example.krishimitra.presentation.mandi_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.example.krishimitra.data.local.entity.MandiPriceEntity
import com.example.krishimitra.data.mappers.toDto
import com.example.krishimitra.presentation.components.CustomizedInputChip
import com.example.krishimitra.presentation.components.MandiPriceItem
import com.example.krishimitra.R
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MandiScreen(
    state: MandiScreenState,
    mandiPrice: LazyPagingItems<MandiPriceEntity>,
    onEvent: (MandiPriceScreenEvent) -> Unit,
) {


    val windowInfo = LocalWindowInfo.current
    val containerWidth = windowInfo.containerSize.width

    var search by rememberSaveable { mutableStateOf("") }
    Scaffold {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(4.dp)
        ) {
            SearchBar(
                inputField = {
                    SearchBarDefaults.InputField(
                        query = search,
                        onQueryChange = {
                            search = it
                        },
                        onSearch = {
                            onEvent(MandiPriceScreenEvent.onSearch(search))
                        },
                        expanded = false,
                        onExpandedChange = {},
                        placeholder = {
                            Text(
                                text = "Search crops"
                            )
                        },
                        trailingIcon = {
                            IconButton(
                                onClick = {}
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.baseline_mic_24),
                                    contentDescription = ""
                                )
                            }
                        }
                    )
                },
                expanded = false,
                onExpandedChange = {},
                content = {

                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp)
            )

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                items(state.listOfStates) {
                    CustomizedInputChip(
                        isSelected = it == state.state,
                        onSelect = {
                            onEvent(MandiPriceScreenEvent.onStateSelect(it))
                        },
                        label = it,
                        onDeselect = {
                            onEvent(MandiPriceScreenEvent.onStateDeselect)
                        }
                    )
                }

            }

            if (state.state.isNotEmpty()) {

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    items(state.listOfDistricts) {
                        CustomizedInputChip(
                            isSelected = it == state.district,
                            onSelect = {
                                onEvent(MandiPriceScreenEvent.onDistrictSelect(it))
                            },
                            label = it,
                            onDeselect = {
                                onEvent(MandiPriceScreenEvent.onDistrictDeselect)
                            }
                        )
                    }

                }
            }
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {

                item {
                    if (mandiPrice.loadState.refresh is LoadState.Loading) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }

                }
                items(mandiPrice.itemCount) { index ->
                    mandiPrice[index]?.let {
                        MandiPriceItem(
                            mandiPrice = it.toDto(),
                            imageSize = if (containerWidth > 600) 100.dp else 80.dp
                        )
                    }
                }
                item {
                    if (mandiPrice.loadState.append is LoadState.Loading) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ){
                            CircularProgressIndicator()
                        }
                    }
                }
            }


        }
    }

}