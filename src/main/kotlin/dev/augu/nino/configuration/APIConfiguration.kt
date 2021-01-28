package dev.augu.nino.configuration

import kotlinx.serialization.Serializable

@Serializable
data class APIConfiguration(
    val host: String? = null,
    val port: Int = 3621
)
