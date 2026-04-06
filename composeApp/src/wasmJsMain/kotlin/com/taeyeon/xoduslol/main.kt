package com.taeyeon.xoduslol

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import com.taeyeon.xoduslol.navigation.bindBrowserHash
import com.taeyeon.xoduslol.navigation.navigationFromInitHash
import com.taeyeon.xoduslol.ui.Galmuri11
import com.taeyeon.xoduslol.util.showCompose
import com.taeyeon.xoduslol.util.stopLoader
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.configureWebResources


@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    configureWebResources {
        resourcePathMapping { path -> "./$path" }
    }

    ComposeViewport(viewportContainerId = "compose-root") {
        if (Galmuri11 != null) {
            LaunchedEffect(Unit) {
                showCompose()
                delay(10_000)
                stopLoader()
            }
        }

        App(
            onNavHostReady = { navController ->
                navController.navigationFromInitHash()
                navController.bindBrowserHash()
            }
        )
    }
}
