package com.example.krishimitra.presentation.auth_screen

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.krishimitra.Constants
import com.example.krishimitra.R
import com.example.krishimitra.domain.farmer_data.UserDataModel
import com.example.krishimitra.presentation.components.LangDropDownMenu
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthScreen(
    state: AuthState,
    changeLanguage: (String) -> Unit,
    signIn: (UserDataModel) -> Unit,
    signUp: (UserDataModel) -> Unit,
    moveToHomeScreen: ()-> Unit,
    getLocation: ()-> Unit
) {

    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { 2 }
    )
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var expandDropDownMenu by remember { mutableStateOf(false) }

    LaunchedEffect(state.isSuccess) {
        if(state.isSuccess) moveToHomeScreen()
    }
    LaunchedEffect(state.isError) {
        if (state.isError != null) {
            Toast.makeText(context, state.isError, Toast.LENGTH_LONG).show()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
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

                            },
                            signIn = signIn,
                            authState = state,
                            context = context
                        )
                    }

                    1 -> {
                        SignUpScreen(
                            moveToSignInScreen = {
                                scope.launch {
                                    pagerState.animateScrollToPage(0)
                                }
                            },
                            signUp = signUp,
                            authState = state,
                            context = context,
                            getLocation = getLocation

                        )

                    }
                }
            }

        }
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 28.dp, end = 16.dp)
        ) {
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
                    text = state.currentLanguage,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            LangDropDownMenu(
                expanded = expandDropDownMenu,
                onDismiss = { expandDropDownMenu = !expandDropDownMenu },
                onClick = {
                    changeLanguage(it)
                },
                langList = Constants.SUPPORTED_LANGUAGES,
                modifier = Modifier
                    .width(200.dp)
                    .height(400.dp)
            )
        }
    }
}
