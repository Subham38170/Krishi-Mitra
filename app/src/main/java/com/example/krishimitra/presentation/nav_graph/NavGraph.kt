package com.example.krishimitra.presentation.nav_graph

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.krishimitra.presentation.auth_screen.AuthScreen
import com.example.krishimitra.presentation.auth_screen.AuthViewModel
import com.example.krishimitra.presentation.buy_sell_screen.BuySellScreen
import com.example.krishimitra.presentation.home_screen.BottomBarInfo
import com.example.krishimitra.presentation.home_screen.HomeScreen
import com.example.krishimitra.presentation.profile_screen.ProfileScreen
import com.google.firebase.auth.FirebaseAuth


@Composable
fun NavGraph(
    navController: NavHostController = rememberNavController()
) {

    val firebaseAuth = FirebaseAuth.getInstance()
    val currentDestination = navController.currentBackStackEntryAsState().value?.destination

    Scaffold(
        bottomBar = {
            if (firebaseAuth.uid != null) {

                NavigationBar(
                    containerColor = Color.White
                ) {
                    BottomBarInfo.bottomBarList.forEach { bottomBarInfo ->
                        val isSelected =
                            currentDestination?.hierarchy?.any { it.hasRoute(bottomBarInfo.route::class) } == true
                        NavigationBarItem(
                            selected = isSelected, onClick = {
                                navController.navigate(bottomBarInfo.route) {
                                    popUpTo(Routes.HomeScreen) { saveState = true }
                                    launchSingleTop = true
                                }

                            }, icon = {
                                Icon(
                                    imageVector = bottomBarInfo.icon,
                                    contentDescription = bottomBarInfo.name
                                )
                            }, colors = NavigationBarItemDefaults.colors(

                            )
                        )
                    }
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
                            popUpTo(0)
                            launchSingleTop = true
                        }
                    },
                    getLocation = authViewModel::getLocation
                )
            }
            composable<Routes.HomeScreen> {
                HomeScreen()

            }
            composable<Routes.ProfileScreen> {
                ProfileScreen(
                    logOut = {
                        firebaseAuth.signOut()
                        navController.navigate(Routes.AuthScreen) {
                            popUpTo(0)
                            launchSingleTop = true
                        }
                    }
                )
            }
            composable<Routes.BuySellScreen> {
                BuySellScreen()
            }

        }
    }

}


