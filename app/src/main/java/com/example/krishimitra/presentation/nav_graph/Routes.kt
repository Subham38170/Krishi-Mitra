package com.example.krishimitra.presentation.nav_graph

import kotlinx.serialization.Serializable

sealed class Routes {

    @Serializable
    data object AuthScreen: Routes()

    @Serializable
    data object HomeScreen: Routes()

    @Serializable
    data object ProfileScreen: Routes()

    @Serializable
    data object BuySellScreen: Routes()

    @Serializable
    data object MandiScreen: Routes()

}