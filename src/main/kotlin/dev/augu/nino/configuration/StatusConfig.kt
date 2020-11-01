package dev.augu.nino.configuration

import kotlinx.serialization.Serializable

@Serializable
data class StatusConfig(
        val status: String,
        val type: Int
)
