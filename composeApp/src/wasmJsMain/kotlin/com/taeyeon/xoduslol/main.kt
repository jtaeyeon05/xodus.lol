package com.taeyeon.xoduslol

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import com.taeyeon.xoduslol.navigation.bindBrowserHash
import com.taeyeon.xoduslol.navigation.navigationFromInitHash
import kotlinx.browser.document


@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    ComposeViewport(document.body!!) {
        App(onNavHostReady = { navController ->
            navController.navigationFromInitHash()
            navController.bindBrowserHash()
        })
    }
}
