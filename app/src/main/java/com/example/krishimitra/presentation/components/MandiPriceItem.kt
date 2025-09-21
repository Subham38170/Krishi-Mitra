package com.example.krishimitra.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.krishimitra.R
import com.example.krishimitra.domain.mandi_data_models.MandiPriceDto

@Composable
fun MandiPriceItem(
    imageSize: Dp = 80.dp,
    mandiPrice: MandiPriceDto
) {


    Card {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(4.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.outline_error_24),
                contentDescription = "",
                modifier = Modifier
                    .size(imageSize)
                    .padding(4.dp)
            )
            Column {
                Text(
                    text = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        ) {
                            append("Commodity : ")
                        }
                        append(mandiPrice.commodity)
                    },
                    overflow = TextOverflow.Ellipsis

                )
                Text(
                    text = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        ) {
                            append("Variety : ")
                        }
                        append(mandiPrice.variety)
                    },
                    overflow = TextOverflow.Ellipsis
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    Text(
                        text = buildAnnotatedString {
                            withStyle(
                                style = SpanStyle(
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            ) {
                                append("Max :")
                            }
                            append("\u20B9")
                            append(mandiPrice.max_price)

                        },
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = buildAnnotatedString {
                            withStyle(
                                style = SpanStyle(
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            ) {
                                append("Min : ")
                            }
                            append("\u20B9")

                            append(mandiPrice.min_price)
                        },
                        overflow = TextOverflow.Ellipsis

                    )
                }
                Text(
                    text = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        ) {
                            append("Modal : ")
                        }
                        append("\u20B9")
                        append(mandiPrice.modal_price)
                    },
                    overflow = TextOverflow.Ellipsis

                )
                Row {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "Location Symbol",
                        modifier = Modifier
                            .size(16.dp)
                    )
                    Text(
                        text = buildAnnotatedString {
                            append(mandiPrice.state)
                            append(", ")
                            append(mandiPrice.district)
                            append(", ")
                            append(mandiPrice.market)
                        },
                        overflow = TextOverflow.Ellipsis

                    )
                }

            }
        }
    }
}
