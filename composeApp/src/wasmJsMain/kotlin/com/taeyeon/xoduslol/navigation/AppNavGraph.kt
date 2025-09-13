package com.taeyeon.xoduslol.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.taeyeon.xoduslol.ui.MainScreen
import com.taeyeon.xoduslol.ui.MoveScreen
import com.taeyeon.xoduslol.ui.StartScreen


fun NavGraphBuilder.appNavGraph(navController: NavController) {
    composable<Screen.Start> { backStackEntry ->
        StartScreen(
            navController = navController,
            screen = backStackEntry.toRoute<Screen.Start>()
        )
    }
    composable<Screen.Main> {
        MainScreen(navController = navController)
    }
    composable<Screen.Move> { backStackEntry ->
        MoveScreen(
            navController = navController,
            screen = backStackEntry.toRoute<Screen.Move>()
        )
    }
}
