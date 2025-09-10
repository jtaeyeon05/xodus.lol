package com.taeyeon.xoduslol

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.taeyeon.xoduslol.ui.AppTheme
import com.taeyeon.xoduslol.ui.MoveScreen
import com.taeyeon.xoduslol.ui.StartScreen


@Composable
fun App(
    onNavHostReady: suspend (NavController) -> Unit = {}
) {
    val navController = rememberNavController()
    LaunchedEffect(navController) {
        onNavHostReady(navController)
    }

    AppTheme(darkTheme = false) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .safeContentPadding()
        ) {
            NavHost(
                navController = navController,
                startDestination = "start"
            ) {
                composable(route = "start") {
                    StartScreen(navController = navController)
                }
                composable(route = "move") {
                    MoveScreen(navController = navController)
                }
                composable(route = "main") {
                    MainScreen(navController = navController)
                }
            }
        }
    }
}
