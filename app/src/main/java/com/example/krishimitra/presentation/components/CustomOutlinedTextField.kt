package com.example.krishimitra.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import com.example.krishimitra.R

@Composable
fun CustomOutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    supportingText: String,
    modifier: Modifier = Modifier,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        modifier = modifier
            .fillMaxWidth(),
        label = {
            Text(
                text = label
            )
        },
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = colorResource(id = R.color.grass_green),
            focusedBorderColor = colorResource(id = R.color.grass_green),
            focusedLabelColor = colorResource(R.color.grass_green),
            unfocusedTextColor = colorResource(id = R.color.grass_green),
            cursorColor = colorResource(id = R.color.grass_green),
        ),
        supportingText = {
            if(supportingText.isNotEmpty()){
                Text(
                    text = supportingText,
                    color = Color.Red
                )
            }
        }
    )

}