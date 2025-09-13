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
import androidx.navigation.toRoute
import com.taeyeon.xoduslol.ui.AppTheme
import com.taeyeon.xoduslol.ui.MainScreen
import com.taeyeon.xoduslol.ui.MoveScreen
import com.taeyeon.xoduslol.ui.StartScreen
import kotlinx.serialization.Serializable


object Screen {
    @Serializable
    data object Start

    @Serializable
    data object Main

    @Serializable
    data class Move(
        val target: String?,
        val newTab: Boolean = false,
    )
}

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
                startDestination = Screen.Start
            ) {
                composable<Screen.Start> {
                    StartScreen(navController = navController)
                }
                composable<Screen.Main> {
                    MainScreen(navController = navController)
                }
                composable<Screen.Move> { backStackEntry ->
                    val move = backStackEntry.toRoute<Screen.Move>()
                    MoveScreen(
                        navController = navController,
                        url = move.target,
                        newTab = move.newTab,
                    )
                }
            }
        }
    }
}
