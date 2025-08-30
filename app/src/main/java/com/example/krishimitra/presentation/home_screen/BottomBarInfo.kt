package com.example.krishimitra.presentation.home_screen

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.krishimitra.presentation.nav_graph.Routes

sealed class BottomBarInfo(
    val name: String,
    val icon: ImageVector,
    val route: Routes
) {
    object HomeScreenIcon : BottomBarInfo(
        name = "Home",
        icon = Icons.Default.Home,
        route = Routes.HomeScreen
    )

    object ProfileScreenIcon : BottomBarInfo(
        name = "Profile",
        icon = Icons.Default.Person,
        route = Routes.ProfileScreen
    )

    object BuySellScreenIcon : BottomBarInfo(
        name = "BuySell",
        icon = Icons.Default.ShoppingCart,
        route = Routes.BuySellScreen
    )

    companion object {
        val bottomBarList = listOf<BottomBarInfo>(
            HomeScreenIcon,
            BuySellScreenIcon,
            ProfileScreenIcon
        )
    }
}
