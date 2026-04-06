package com.taeyeon.xoduslol.navigation

import androidx.navigation.ExperimentalBrowserHistoryApi
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.bindToBrowserNavigation
import androidx.navigation.toRoute
import com.taeyeon.xoduslol.util.buildQuery
import kotlinx.browser.window
import org.w3c.dom.url.URLSearchParams


fun parseHash(): Screen {
    val identifier = window.location.hash.removePrefix("#").substringBefore("?")
    val params = URLSearchParams(window.location.hash.substringAfter("?", "").toJsString())

    val screen = Screen.fromIdentifier(
        identifier = identifier,
        params = params
    )

    return screen
}

@OptIn(ExperimentalBrowserHistoryApi::class)
suspend fun NavController.bindBrowserHash() {
    bindToBrowserNavigation(
        getBackStackEntryRoute = { entry ->
            val identifier: String
            val mapQuery = mutableMapOf<String, String>()
            val listQuery = mutableListOf<String>()

            val destination = entry.destination
            identifier = when {
                destination.hasRoute<Screen.Home>() -> {
                    val screen = entry.toRoute<Screen.Home>()
                    if (screen.partyMode) listQuery.add("partyMode")
                    screen.identifier()
                }
                destination.hasRoute<Screen.Corridor>() -> Screen.Corridor.identifier()
                destination.hasRoute<Screen.AudioPlayground>() -> Screen.AudioPlayground.identifier()
                destination.hasRoute<Screen.Move>() -> {
                    val screen = entry.toRoute<Screen.Move>()
                    screen.target?.let { mapQuery["target"] = screen.target }
                    if (screen.newTab) listQuery.add("newTab")
                    screen.identifier()
                }
                destination.hasRoute<Screen.Plain>() -> {
                    val screen = entry.toRoute<Screen.Plain>()
                    screen.message?.let { mapQuery["message"] = screen.message }
                    screen.identifier()
                }
                else -> ""
            }

            "#$identifier" + buildQuery(
                mapQuery = mapQuery,
                listQuery = listQuery
            )
        }
    )
}
