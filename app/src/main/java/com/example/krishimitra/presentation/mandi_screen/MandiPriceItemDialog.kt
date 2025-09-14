package com.example.krishimitra.presentation.mandi_screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ElevatedCard
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.krishimitra.domain.mandi_data_models.MandiPriceDto


@Preview(showBackground = true)
@Composable
fun MandiPriceItemDialog(
    mandiPrice: MandiPriceDto = MandiPriceDto(
        arrival_date = "12/34/44",
        commodity = "Bhindi",
        district = "Ganjam",
        grade = "Faa",
        market = "Berhampur",
        max_price = "2000",
        min_price = "1200",
        modal_price = "1400",
        state = "odisha",
        variety = "lslsflksf"
    )
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .height(400.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column {

        }

    }

}