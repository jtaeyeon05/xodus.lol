package com.taeyeon.xoduslol.navigation

import com.taeyeon.xoduslol.util.decodeURIComponent
import kotlinx.serialization.Serializable
import org.w3c.dom.url.URLSearchParams


sealed interface Screen {
    @Serializable
    data class Home(
        val partyMode: Boolean = false,
    ): Screen

    @Serializable
    data object Corridor: Screen

    @Serializable
    data object AudioPlayground: Screen

    @Serializable
    data class Move(
        val target: String? = null,
        val newTab: Boolean = false,
    ): Screen

    @Serializable
    data class Plain(
        val message: String? = null,
    ): Screen

    fun identifier(): String = when (this) {
        is Home -> "home"
        is Corridor -> "corridor"
        is AudioPlayground -> "audioPlayground"
        is Move -> "move"
        is Plain -> "plain"
    }

    companion object {
        fun fromIdentifier(
            identifier: String,
            params: URLSearchParams? = null
        ): Screen = when (identifier) {
            "", "home" -> {
                val partyMode = params?.has("partyMode") ?: false
                Home(
                    partyMode = partyMode
                )
            }
            "corridor" -> Corridor
            "audioPlayground" -> AudioPlayground
            "move" -> {
                val target = params?.get("target")?.let { decodeURIComponent(it) }
                val newTab = params?.has("newTab") ?: false
                Move(
                    target = target,
                    newTab = newTab
                )
            }
            "plain" -> {
                val message = params?.get("message")?.let { decodeURIComponent(it) }
                Plain(
                    message = message
                )
            }
            else -> {
                Plain(
                    message = "음... \"$identifier\"라는 주소는 존재하지 않아."
                )
            }
        }
    }
}
