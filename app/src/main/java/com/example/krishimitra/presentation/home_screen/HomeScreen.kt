package com.example.krishimitra.presentation.home_screen

import android.Manifest
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomAppBarScrollBehavior
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import com.example.krishimitra.R
import com.example.krishimitra.domain.farmer_data.UserDataModel
import com.example.krishimitra.domain.weather_models.WeatherApiResponseItem
import com.example.krishimitra.presentation.components.shimmerEffect
import com.example.krishimitra.presentation.components.top_app_bars.HomeScreenTopBar
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import java.io.File

@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onEvent: (HomeScreenEvent) -> Unit,
    state: HomeScreenState,
    moveToMandiScreen: () -> Unit,
    moveToDiseasePredictionScreen: (Uri?) -> Unit,
    moveToKrishiBazar: () -> Unit,
    scrollBehavior: BottomAppBarScrollBehavior
) {


    val context = LocalContext.current
    var showDiseasePredictionAlertDialog by remember { mutableStateOf(false) }


    var imageUri by remember { mutableStateOf<Uri?>(null) }

    val photoFile = remember {
        File(context.cacheDir, "captured_image.jpg").apply {
            createNewFile()
        }
    }

    val uri = FileProvider.getUriForFile(
        context, "${context.packageName}.provider", photoFile
    )

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(), onResult = { sucess ->
            if (sucess) imageUri = uri

        })


    LaunchedEffect(imageUri) {
        imageUri?.let {
            showDiseasePredictionAlertDialog = false
            moveToDiseasePredictionScreen(it)
        }
    }

    val galleryPermission =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) Manifest.permission.READ_MEDIA_IMAGES
        else Manifest.permission.READ_EXTERNAL_STORAGE
    val imagePermissionState = rememberPermissionState(galleryPermission)
    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(), onResult = {
            imageUri = it
        })

    if (showDiseasePredictionAlertDialog) {

        AlertDialog(
            containerColor = colorResource(id = R.color.light_green),
            onDismissRequest = {
                showDiseasePredictionAlertDialog = !showDiseasePredictionAlertDialog
            }, confirmButton = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    TextButton(
                        onClick = {
                            if (imagePermissionState.status.isGranted) {
                                galleryLauncher.launch("image/*")
                            } else {
                                imagePermissionState.launchPermissionRequest()
                            }
                        }) {
                        Text(
                            text = stringResource(id = R.string.upload_image)
                        )
                    }
                    TextButton(
                        onClick = {
                            if (cameraPermissionState.status.isGranted) {
                                launcher.launch(uri)
                            } else {
                                cameraPermissionState.launchPermissionRequest()
                            }
                        }) {
                        Text(
                            text = "Click Photo"
                        )
                    }
                }
            })
    }


    Scaffold(
        containerColor = colorResource(id = R.color.light_green),
        topBar = {
            HomeScreenTopBar(
                currentLanguage = state.currentLanguage, onLanguageChange = {
                    onEvent(HomeScreenEvent.ChangeLanguage(it))
                })
        },

        ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .background(colorResource(id = R.color.light_green))
                .nestedScroll(scrollBehavior.nestedScrollConnection)
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            item {
                HorizontalDivider(thickness = 1.dp, color = Color.White)
            }
            item {
                if (state.weatherData.isNotEmpty()) {
                    WeatherCard(
                        modifier = Modifier
                            .fillMaxWidth(),
                        userData = state.userData,
                        weatherApiResponseItem = state.weatherData[0]
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(220.dp)
                            .background(
                                brush = Brush
                                    .verticalGradient(
                                        colors = listOf(
                                            colorResource(id = R.color.slight_dark_green),
                                            colorResource(id = R.color.slight_dark_green),
                                            colorResource(id = R.color.slight_dark_green),
                                            colorResource(id = R.color.light_green),
                                            Color.White
                                        )
                                    )
                            )
                            .shimmerEffect(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Loading...",
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
            item {
                if (state.schemeBannersList.isNotEmpty()) {
                    AutoImageSchemeSlider(

                        banners = state.schemeBannersList,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    )
                }
            }
            item {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    CustomizedHomeButton(
                        onClick = {
                            showDiseasePredictionAlertDialog = true
                        },
                        painter = painterResource(id = R.drawable.plant_disease),
                        modifier = Modifier
                            .weight(1f)
                            .shadow(2.dp, RoundedCornerShape(12.dp)),
                        text = stringResource(id = R.string.disease_prediction)
                    )
                    CustomizedHomeButton(
                        onClick = {},
                        painter = painterResource(id = R.drawable.soil_health),
                        modifier = Modifier
                            .weight(1f)
                            .shadow(2.dp, RoundedCornerShape(12.dp)),
                        text = stringResource(id = R.string.crop_recommendataion)
                    )

                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    CustomizedHomeButton(
                        onClick = moveToMandiScreen,
                        painter = painterResource(id = R.drawable.mandi_price),
                        modifier = Modifier
                            .weight(1f)
                            .shadow(2.dp, RoundedCornerShape(12.dp)),
                        text = stringResource(id = R.string.mandi_price)
                    )
                    CustomizedHomeButton(
                        onClick = moveToKrishiBazar,
                        painter = painterResource(id = R.drawable.buy_sell_crop),
                        modifier = Modifier
                            .weight(1f)
                            .shadow(2.dp, RoundedCornerShape(12.dp)),
                        text = stringResource(id = R.string.krishi_bazar)
                    )

                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = stringResource(id = R.string.agri_news),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(16.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))

            }
            item {
                if (state.krishiNewsBannerList.isNotEmpty()) {
                    AutoImageNewsSlider(
                        banners = state.krishiNewsBannerList,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    )
                }
            }
            item {
                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}


@Composable
fun CustomizedHomeButton(
    onClick: () -> Unit, painter: Painter, modifier: Modifier = Modifier, text: String
) {

    Column(
        modifier = modifier
            .background(
                brush = Brush.sweepGradient(
                    colors = listOf(Color(0xFF94FD72), Color(0xFFC4D9C8), Color(0xFFD4DBD5))
                ), shape = RoundedCornerShape(12.dp)
            )
            .clip(RoundedCornerShape(12.dp))
            .padding(2.dp)
            .clickable(
                enabled = true,
                onClick = onClick,
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            )

    ) {
        Card {
            Image(
                painter = painter,
                contentDescription = "Plant Disease",
                modifier = Modifier
                    .height(120.dp)
                    .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)),

                contentScale = ContentScale.FillBounds
            )
        }
        Text(

            text = text,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Transparent),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
    }
}


@Composable
fun WeatherCard(
    modifier: Modifier,
    userData: UserDataModel,
    weatherApiResponseItem: WeatherApiResponseItem
) {

    Column(
        modifier = modifier
            .height(220.dp)
            .background(
                brush = Brush
                    .verticalGradient(
                        colors = listOf(
                            colorResource(id = R.color.slight_dark_green),
                            colorResource(id = R.color.slight_dark_green),
                            colorResource(id = R.color.slight_dark_green),
                            colorResource(id = R.color.light_green),
                            Color.White
                        )
                    )
            )
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier
                .height(40.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = "Location",
                tint = Color.White
            )
            Text(
                text = buildAnnotatedString {
                    append(userData.village)
                    append(", ")
                    append(userData.district)
                    append(", ")
                    append(userData.state)
                },
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            items(weatherApiResponseItem.hourly) { data ->
                WeatherIcon(
                    time = data.time,
                    drawable = getWeatherIcon(data.condition),
                    temperature = "${data.temperature} \u00B0C"
                )
            }
        }
    }


}

fun getWeatherIcon(condition: String): Int {
    return when (condition.trim().lowercase()) {
        "clear" -> R.mipmap.sunny
        "sunny" -> R.mipmap.sunny
        "partly cloudy" -> R.mipmap.cloudy_sunny
        "patchy rain nearby" -> R.mipmap.rainy
        "patchy light drizzle" -> R.mipmap.rain
        "light rain shower" -> R.mipmap.rain
        "thundery outbreaks in nearby" -> R.mipmap.storm
        else -> R.drawable.outline_error_24
    }
}


@Composable
fun WeatherIcon(
    time: String,
    @DrawableRes drawable: Int,
    temperature: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .height(100.dp)
            .width(60.dp)
            .padding(horizontal = 8.dp)
    ) {
        Text(
            text = temperature,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Image(
            painter = painterResource(id = drawable),
            contentDescription = "Weather",
            modifier = Modifier
                .size(52.dp)

        )
        Text(
            text = time,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }

}