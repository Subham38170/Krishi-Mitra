package com.example.krishimitra

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.krishimitra.presentation.components.LangDropDownMenu
import com.example.krishimitra.presentation.nav_graph.NavGraph
import com.example.krishimitra.ui.theme.KrishiMitraTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KrishiMitraTheme {
                val navController = rememberNavController()
                NavGraph(navController = navController)
            }
        }
    }
}

