package com.taeyeon.xoduslol.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.taeyeon.xoduslol.ui.MainScreen
import com.taeyeon.xoduslol.ui.MoveScreen
import com.taeyeon.xoduslol.ui.StartScreen


fun NavGraphBuilder.appNavGraph(navController: NavController) {
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
