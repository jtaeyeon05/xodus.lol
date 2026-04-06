package com.taeyeon.xoduslol.util

import kotlinx.browser.document
import kotlinx.browser.window
import org.w3c.dom.HTMLElement


external fun encodeURIComponent(str: String): String

external fun decodeURIComponent(str: String): String

fun stopLoader() {
    js("if (window.stopLoader) window.stopLoader();")
}

fun showCompose() {
    val composeRoot = document.getElementById("compose-root") as HTMLElement
    composeRoot.style.opacity = "1"
    composeRoot.style.zIndex = "2"
}

fun replaceHash(newHash: String) {
    window.history.replaceState(window.history.state, "", newHash)
}

fun Float.floorMultiple(step: Float) = (this / step).toInt() * step

fun buildQuery(
    mapQuery: Map<String, String?> = mapOf(),
    listQuery: List<String?> = listOf(),
): String {
    val parts = mutableListOf<String>()
    for ((key, value) in mapQuery) {
        if (value != null) {
            parts.add("$key=${encodeURIComponent(value)}")
        }
    }
    for (query in listQuery) {
        if (query != null) {
            parts.add(query)
        }
    }
    return if (parts.isEmpty()) "" else "?${parts.joinToString("&")}"
}
