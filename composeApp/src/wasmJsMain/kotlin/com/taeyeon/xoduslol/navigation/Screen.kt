package com.taeyeon.xoduslol.navigation

import kotlinx.serialization.Serializable

object Screen {
    @Serializable
    data object Start

    @Serializable
    data object Main

    @Serializable
    data class Move(
        val target: String?,
        val newTab: Boolean = false,
    )
}