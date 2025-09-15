package com.taeyeon.xoduslol.navigation

import kotlinx.serialization.Serializable

object Screen {
    @Serializable
    data class Home(
        val partyMode: Boolean = false,
    )

    @Serializable
    data object AudioPlayground

    @Serializable
    data class Move(
        val target: String? = null,
        val newTab: Boolean = false,
    )

    @Serializable
    data class Plain(
        val message: String? = null,
    )
}