package com.taeyeon.xoduslol.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.taeyeon.xoduslol.ui.screen.AudioPlaygroundScreen
import com.taeyeon.xoduslol.ui.screen.CorridorScreen
import com.taeyeon.xoduslol.ui.screen.PlainScreen
import com.taeyeon.xoduslol.ui.screen.MoveScreen
import com.taeyeon.xoduslol.ui.screen.HomeScreen


fun NavGraphBuilder.appNavGraph(navController: NavController) {
    composable<Screen.Home> { backStackEntry ->
        HomeScreen(
            navController = navController,
            screen = backStackEntry.toRoute<Screen.Home>()
        )
    }
    composable<Screen.Corridor> {
        CorridorScreen(
            navController = navController
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
