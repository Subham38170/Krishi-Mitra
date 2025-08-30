package com.example.krishimitra.presentation.auth_screen

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.krishimitra.Constants
import com.example.krishimitra.R
import com.example.krishimitra.presentation.components.LangDropDownMenu
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthScreen() {
    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { 2 }
    )
    val scope = rememberCoroutineScope()
    var expandDropDownMenu by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(it)
    ) {

        Image(
            painter = painterResource(id = R.drawable.background_image),
            contentDescription = "Back ground Image",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillHeight
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White.copy(alpha = 0.2f))
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LangDropDownMenu(
                expanded = true,
                onDismiss = { expandDropDownMenu = !expandDropDownMenu },
                onClick = {
                    Log.d("LANG", it.nativeName)
                },
                langList = Constants.SUPPORTED_LANGUAGES,
                modifier = Modifier
                    .width(200.dp)
            )
            HorizontalPager(
                state = pagerState
            ) {
                when (pagerState.currentPage) {
                    0 -> {
                        SignInScreen(
                            moveToSignUpScreen = {
                                scope.launch {
                                    pagerState.animateScrollToPage(1)
                                }

                            }
                        )
                    }

                    1 -> {
                        SignUpScreen(
                            moveToSignInScreen = {
                                scope.launch {
                                    pagerState.animateScrollToPage(0)
                                }
                            }
                        )
                    }
                }
            }

        }

    }
}
