package com.taeyeon.xoduslol.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.taeyeon.xoduslol.ui.screen.AudioPlaygroundScreen
import com.taeyeon.xoduslol.ui.screen.PlainScreen
import com.taeyeon.xoduslol.ui.screen.MoveScreen
import com.taeyeon.xoduslol.ui.screen.StartScreen


fun NavGraphBuilder.appNavGraph(navController: NavController) {
    composable<Screen.Start> { backStackEntry ->
        StartScreen(
            navController = navController,
            screen = backStackEntry.toRoute<Screen.Start>()
        )
    }
    composable<Screen.AudioPlayground> {
        AudioPlaygroundScreen(
            navController = navController
        )
    }
    composable<Screen.Move> { backStackEntry ->
        MoveScreen(
            navController = navController,
            screen = backStackEntry.toRoute<Screen.Move>()
        )
    }
    composable<Screen.Plain> { backStackEntry ->
        PlainScreen(
            navController = navController,
            screen = backStackEntry.toRoute<Screen.Plain>()
        )
    }
}
