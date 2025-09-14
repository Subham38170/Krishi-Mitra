package com.example.krishimitra.presentation.components.top_app_bars

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.krishimitra.Constants


import com.example.krishimitra.R
import com.example.krishimitra.presentation.components.LangDropDownMenu

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenTopBar(
    currentLanguage: String,
    onLanguageChange: (String) -> Unit
) {
    var expandDropDownMenu by remember { mutableStateOf(false) }
    TopAppBar(
        title = {
            Text(
                text = stringResource(id = R.string.app_name)
            )
        },
        actions = {

            Row {
                Button(
                    onClick = {
                        expandDropDownMenu = true
                    },
                    shape = RoundedCornerShape(4.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White.copy(alpha = 0.7f),
                        contentColor = Color.Black
                    )
                ) {
                    Text(
                        text = currentLanguage,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                LangDropDownMenu(
                    expanded = expandDropDownMenu,
                    onDismiss = { expandDropDownMenu = !expandDropDownMenu },
                    onClick = {
                        onLanguageChange(it)
                    },
                    langList = Constants.SUPPORTED_LANGUAGES,
                    modifier = Modifier
                        .width(200.dp)
                        .height(400.dp)
                )
            }
            FilledIconButton(
                onClick = {}
            ) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = "Notification Icon",
                )
            }
        }

    )

}
