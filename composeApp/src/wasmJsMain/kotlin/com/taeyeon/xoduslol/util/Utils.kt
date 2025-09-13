package com.taeyeon.xoduslol.util

import kotlinx.browser.window


@JsFun("str => encodeURIComponent(str)")
external fun encodeURIComponent(str: String): String

@JsFun("str => decodeURIComponent(str)")
external fun decodeURIComponent(str: String): String

fun replaceHash(newHash: String) =
    window.history.replaceState(window.history.state, "", newHash)

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
