package com.example.krishimitra.presentation.nav_graph


import android.app.Activity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.krishimitra.R
import com.example.krishimitra.presentation.auth_screen.AuthScreen
import com.example.krishimitra.presentation.auth_screen.AuthViewModel
import com.example.krishimitra.presentation.buy_sell_screen.BuySellScreen
import com.example.krishimitra.presentation.buy_sell_screen.BuySellScreenViewModel
import com.example.krishimitra.presentation.community_screen.ComunityMainScreen
import com.example.krishimitra.presentation.community_screen.StateCommunityScreen
import com.example.krishimitra.presentation.disease_prediction_screen.DiseasePredictionScreen
import com.example.krishimitra.presentation.disease_prediction_screen.DiseasePredictionViewModel
import com.example.krishimitra.presentation.home_screen.HomeScreen
import com.example.krishimitra.presentation.home_screen.HomeScreenViewModel
import com.example.krishimitra.presentation.mandi_screen.MandiScreen
import com.example.krishimitra.presentation.mandi_screen.MandiScreenViewModel
import com.example.krishimitra.presentation.profile_screen.ProfileScreen
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavGraph(
    navController: NavHostController = rememberNavController(),
    activity: Activity
) {

    val firebaseAuth = FirebaseAuth.getInstance()
    val currentDestination = navController.currentBackStackEntryAsState().value?.destination

    val scrollBehavior = BottomAppBarDefaults.exitAlwaysScrollBehavior()

    Scaffold(
        bottomBar = {
            if (firebaseAuth.uid != null && navController.currentDestination != Routes.StateCommunityScreen && navController.currentDestination != Routes.CommunityMainScreen && navController.currentDestination != Routes.DiseasePredictionScreen) {
                AnimatedVisibility(
                    visible = scrollBehavior.state.contentOffset >= -12f,
                    enter = slideInVertically(
                        initialOffsetY = { it },
                        animationSpec = tween(durationMillis = 50)
                    ) + fadeIn(),

                    exit = slideOutVertically(
                        targetOffsetY = { it },
                        animationSpec = tween(durationMillis = 150)
                    ) + fadeOut()
                ) {
                    CustomizedBottomAppBar(
                        navController = navController,
                        currentDestination = currentDestination
                    )
                }


            }
        }) { innerpadding ->
        NavHost(
            navController = navController,
            startDestination = if (firebaseAuth.uid == null) Routes.AuthScreen else Routes.HomeScreen
        ) {
            composable<Routes.AuthScreen> {
                val authViewModel = hiltViewModel<AuthViewModel>()
                AuthScreen(
                    state = authViewModel.state.collectAsStateWithLifecycle().value,
                    changeLanguage = authViewModel::changeLanguage,
                    signIn = authViewModel::signIn,
                    signUp = authViewModel::signUp,
                    moveToHomeScreen = {
                        navController.navigate(Routes.HomeScreen) {
                            popUpTo(0) { saveState = true }
                            launchSingleTop = true
                        }
                    },
                    getLocation = authViewModel::getLocation,
                    onEnableLocationPermission = {
                        authViewModel.onEnableLocationPermission(activity)
                    },
                    errorFlow = authViewModel.error
                )
            }
            composable<Routes.HomeScreen> {
                val homeScreenViewModel = hiltViewModel<HomeScreenViewModel>()
                HomeScreen(
                    state = homeScreenViewModel.state.collectAsStateWithLifecycle().value,
                    onEvent = homeScreenViewModel::onEvent,
                    moveToMandiScreen = {
                        navController.navigate(Routes.MandiScreen) { launchSingleTop = true }
                    },
                    moveToDiseasePredictionScreen = { uri ->
                        navController.navigate(Routes.DiseasePredictionScreen(uri.toString())) {
                            launchSingleTop = true
                        }
                    },
                    scrollBehavior = scrollBehavior,
                    moveToKrishiBazar = {
                        navController.navigate(Routes.BuySellScreen) {
                            launchSingleTop = true
                        }
                    }
                )

            }
            composable<Routes.ProfileScreen> {
                ProfileScreen(
                    logOut = {
                        firebaseAuth.signOut()
                        navController.navigate(Routes.AuthScreen) {
                            popUpTo(0) { saveState = true }
                            launchSingleTop = true
                        }
                    }
                )
            }
            composable<Routes.BuySellScreen> {
                val buySellViewModel = hiltViewModel<BuySellScreenViewModel>()
                BuySellScreen(
                    buyScreenState = buySellViewModel.buyScreenState.collectAsStateWithLifecycle().value,
                    onEvent = buySellViewModel::onEvent,
                    sellScreenState = buySellViewModel.sellScreenState.collectAsStateWithLifecycle().value,
                    event = buySellViewModel.event,
                    scrollBahavior = scrollBehavior
                )
            }

            composable<Routes.MandiScreen> {
                val mandiViewModel = hiltViewModel<MandiScreenViewModel>()
                MandiScreen(
                    state = mandiViewModel.state.collectAsStateWithLifecycle().value,
                    mandiPrice = mandiViewModel.pagingData.collectAsLazyPagingItems(),
                    onEvent = mandiViewModel::onEvent,
                    scrollBehavior = scrollBehavior
                )
            }
            composable<Routes.DiseasePredictionScreen> {
                val diseasePredictionViewModel = hiltViewModel<DiseasePredictionViewModel>()
                val data = it.toRoute<Routes.DiseasePredictionScreen>()
                DiseasePredictionScreen(
                    imageUri = data.imageUri?.toUri(),
                    moveBackToScreen = {
                        navController.popBackStack()
                    },
                    state = diseasePredictionViewModel.state.collectAsStateWithLifecycle().value,
                    event = diseasePredictionViewModel.event,
                    onEvent = diseasePredictionViewModel::onEvent
                )
            }

            composable<Routes.CommunityMainScreen> {
                ComunityMainScreen(
                    moveToMessageScreen = { name ->
                        navController.navigate(Routes.StateCommunityScreen(name)) {

                            launchSingleTop = true

                        }
                    }
                )
            }
            composable<Routes.StateCommunityScreen> {
                val data = it.toRoute<Routes.StateCommunityScreen>()
                StateCommunityScreen(
                    state = data.state,
                    onBackClick = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }

}


@Composable
fun CustomizedBottomAppBar(
    navController: NavHostController,
    currentDestination: NavDestination?
) {
    BottomAppBar(
        modifier = Modifier
            .height(100.dp),
        containerColor = colorResource(id = R.color.light_green)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
            ,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            BottomBarInfo.bottomBarList.forEach { bottomBarInfo ->
                val isSelected = currentDestination?.hierarchy?.any { it.hasRoute(bottomBarInfo.route::class) } == true

                IconButton(
                    onClick = {
                        if (!isSelected) {
                            navController.navigate(bottomBarInfo.route) {
                                popUpTo(Routes.HomeScreen){saveState=true}
                                launchSingleTop = true
                            }
                        }
                    },
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = Color.Transparent,
                        contentColor = if (isSelected) colorResource(
                            id = R.color.slight_dark_green
                        ) else Color.Black
                    )

                ) {
                    Icon(
                        imageVector = bottomBarInfo.icon,
                        contentDescription = bottomBarInfo.name
                    )

                }
            }

        }
    }
}


