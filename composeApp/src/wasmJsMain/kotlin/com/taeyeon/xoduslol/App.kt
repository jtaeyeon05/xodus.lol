package com.taeyeon.xoduslol

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.taeyeon.xoduslol.navigation.appNavGraph
import com.taeyeon.xoduslol.navigation.bindBrowserHash
import com.taeyeon.xoduslol.navigation.parseInitHash
import com.taeyeon.xoduslol.ui.AppTheme


@Composable
fun App() {
    val navController = rememberNavController()
    val startScreen = remember { parseInitHash() }

    LaunchedEffect(navController) {
        navController.bindBrowserHash()
    }

    AppTheme(darkTheme = false) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .safeContentPadding()
        ) {
            NavHost(
                navController = navController,
                startDestination = startScreen
            ) {
                appNavGraph(navController = navController)
            }
        }
    }
}
