package com.taeyeon.xoduslol.navigation

import androidx.navigation.ExperimentalBrowserHistoryApi
import androidx.navigation.NavController
import androidx.navigation.bindToBrowserNavigation
import androidx.navigation.toRoute
import com.taeyeon.xoduslol.util.buildQuery
import com.taeyeon.xoduslol.util.decodeURIComponent
import kotlinx.browser.window
import org.w3c.dom.url.URLSearchParams


fun NavController.navigationFromInitHash() {
    val initHash = window.location.hash.removePrefix("#")
    val params = URLSearchParams(initHash.substringAfter("?", "").toJsString())

    if (initHash.isBlank()) return
    when (initHash.substringBefore("?")) {
        "start" -> navigate(Screen.Start)
        "main" -> navigate(Screen.Main)
        "move" -> {
            val target = params.get("target")?.let { decodeURIComponent(it) }
            val newTab = params.has("newTab")
            navigate(
                Screen.Move(
                    target = target,
                    newTab = newTab
                )
            )
        }
    }
}

@OptIn(ExperimentalBrowserHistoryApi::class)
suspend fun NavController.bindBrowserHash() {
    bindToBrowserNavigation(
        getBackStackEntryRoute = { entry ->
            val route = entry.destination.route.orEmpty()
            when {
                route.startsWith(Screen.Start.serializer().descriptor.serialName) -> {
                    "#start"
                }
                route.startsWith(Screen.Main.serializer().descriptor.serialName) -> {
                    "#main"
                }
                route.startsWith(Screen.Move.serializer().descriptor.serialName) -> {
                    val args = entry.toRoute<Screen.Move>()
                    "#move" + buildQuery(
                        mapQuery = mapOf("target" to args.target),
                        listQuery = listOf("newTab".takeIf { args.newTab })
                    )
                }
                else -> ""
            }
        }
    )
}
