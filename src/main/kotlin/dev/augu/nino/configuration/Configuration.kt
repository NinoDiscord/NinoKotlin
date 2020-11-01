package dev.augu.nino.configuration

import kotlinx.serialization.Serializable

@Serializable
data class Configuration(
        val status: StatusConfig? = null,
        val base: BaseConfiguration
)
