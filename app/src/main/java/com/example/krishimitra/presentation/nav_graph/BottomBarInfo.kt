package com.example.krishimitra.presentation.nav_graph

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomBarInfo(
    val name: String,
    val icon: ImageVector,
    val route: Routes
) {
    object HomeScreenInfo : BottomBarInfo(
        name = "Home",
        icon = Icons.Default.Home,
        route = Routes.HomeScreen
    )

    object ProfileScreenInfo : BottomBarInfo(
        name = "Profile",
        icon = Icons.Default.Person,
        route = Routes.ProfileScreen
    )

    object BuySellScreenInfo : BottomBarInfo(
        name = "BuySell",
        icon = Icons.Default.ShoppingCart,
        route = Routes.BuySellScreen
    )

    object MandiPriceScreenInfo : BottomBarInfo(
        name = "MandiPrice",
        icon = Icons.Default.MailOutline,
        route = Routes.MandiScreen
    )

    companion object {
        val bottomBarList = listOf<BottomBarInfo>(
            HomeScreenInfo,
            BuySellScreenInfo,
            MandiPriceScreenInfo,

            ProfileScreenInfo
        )
    }
}