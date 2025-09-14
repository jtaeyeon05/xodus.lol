package com.taeyeon.xoduslol.navigation

import kotlinx.serialization.Serializable

object Screen {
    @Serializable
    data class Start(
        val partyMode: Boolean = false,
    )

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