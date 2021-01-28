package dev.augu.nino.configuration

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class Environment {
    @SerialName("development")
    Development,

    @SerialName("production")
    Production
}
