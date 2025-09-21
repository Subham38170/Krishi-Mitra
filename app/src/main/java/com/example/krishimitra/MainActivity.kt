package com.example.krishimitra

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.example.krishimitra.data.repo.LanguageManager
import com.example.krishimitra.presentation.nav_graph.NavGraph
import com.example.krishimitra.presentation.ui.theme.KrishiMitraTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var languageManager: LanguageManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            languageManager.getLanguage().collect {
                languageManager.updateLanguage(it)
            }
        }

        enableEdgeToEdge()
        setContent {
            KrishiMitraTheme {
                val navController = rememberNavController()
                NavGraph(
                    navController = navController,
                    activity = this
                )
            }
        }
    }
}

