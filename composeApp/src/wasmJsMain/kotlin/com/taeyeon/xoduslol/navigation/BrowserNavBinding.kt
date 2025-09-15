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
        "home", "" -> {
            val partyMode = params.has("partyMode")
            navigate(
                Screen.Home(
                    partyMode = partyMode
                )
            )
        }
        "corridor" -> {
            navigate(Screen.Corridor)
        }
        "audioPlayground" -> {
            navigate(Screen.AudioPlayground)
        }
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
        "plain" -> {
            val message = params.get("message")?.let { decodeURIComponent(it) }
            navigate(
                Screen.Plain(
                    message = message
                )
            )
        }
        else -> {
            navigate(
                Screen.Plain(
                    message = "음... \"$initHash\"라는 주소는 존재하지 않아."
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
                route.startsWith(Screen.Home.serializer().descriptor.serialName) -> {
                    val args = entry.toRoute<Screen.Home>()
                    "#home" + buildQuery(
                        listQuery = listOf("partyMode".takeIf { args.partyMode })
                    )
                }
                route.startsWith(Screen.Corridor.serializer().descriptor.serialName) -> {
                    "#corridor"
                }
                route.startsWith(Screen.AudioPlayground.serializer().descriptor.serialName) -> {
                    "#audioPlayground"
                }
                route.startsWith(Screen.Move.serializer().descriptor.serialName) -> {
                    val args = entry.toRoute<Screen.Move>()
                    "#move" + buildQuery(
                        mapQuery = mapOf("target" to args.target),
                        listQuery = listOf("newTab".takeIf { args.newTab })
                    )
                }
                route.startsWith(Screen.Plain.serializer().descriptor.serialName) -> {
                    val args = entry.toRoute<Screen.Plain>()
                    "#plain" + buildQuery(
                        mapQuery = mapOf("message" to args.message),
                    )
                }
                else -> ""
            }
        }
    )
}
