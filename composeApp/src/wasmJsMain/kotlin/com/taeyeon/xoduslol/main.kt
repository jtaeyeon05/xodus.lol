package com.taeyeon.xoduslol

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import androidx.navigation.ExperimentalBrowserHistoryApi
import androidx.navigation.bindToBrowserNavigation
import androidx.navigation.toRoute
import com.taeyeon.xoduslol.util.decodeURIComponent
import com.taeyeon.xoduslol.util.encodeURIComponent
import kotlinx.browser.document
import kotlinx.browser.window
import org.w3c.dom.url.URLSearchParams


fun buildQuery(
    mapQuery: Map<String, String?> = mapOf(),
    listQuery: List<String?> = listOf(),
): String {
    val parts = mutableListOf<String>()
    for ((key, value) in mapQuery) {
        if (value != null) {
            parts.add("$key=$value")
        }
    }
    for (query in listQuery) {
        if (query != null) {
            parts.add(query)
        }
    }
    return if (parts.isEmpty()) "" else "?${parts.joinToString("&")}"
}

@OptIn(
    ExperimentalComposeUiApi::class,
    ExperimentalBrowserHistoryApi::class
)
fun main() {
    ComposeViewport(document.body!!) {
        App(onNavHostReady = { navController ->
            val initHash = window.location.hash.removePrefix("#")
            if (initHash.isNotBlank()) {
                when (initHash.substringBefore("?")) {
                    "start" -> navController.navigate(Screen.Start)
                    "main" -> navController.navigate(Screen.Main)
                    "move" -> {
                        val query = initHash.substringAfter("?", "").toJsString()
                        val target = URLSearchParams(query).get("target")?.let { decodeURIComponent(it) }
                        val newTab = URLSearchParams(query).has("newTab")
                        navController.navigate(
                            Screen.Move(
                                target = target,
                                newTab = newTab,
                            )
                        )
                    }
                }
            }

            navController.bindToBrowserNavigation(
                getBackStackEntryRoute = { entry ->
                    when {
                        entry.destination.route.orEmpty()
                            .startsWith(Screen.Start.serializer().descriptor.serialName) -> {
                            "#start"
                        }
                        entry.destination.route.orEmpty()
                            .startsWith(Screen.Main.serializer().descriptor.serialName) -> {
                            "#main"
                        }
                        entry.destination.route.orEmpty()
                            .startsWith(Screen.Move.serializer().descriptor.serialName) -> {
                            val args = entry.toRoute<Screen.Move>()
                            "#move" + buildQuery(
                                mapQuery = mapOf("target" to if (args.target != null) encodeURIComponent(args.target) else null),
                                listQuery = listOf(if (args.newTab) "newTab" else null)
                            )
                        }
                        else -> ""
                    }
                }
            )
        })
    }
}
