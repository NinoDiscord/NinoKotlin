package dev.augu.nino.configuration

import kotlinx.serialization.Serializable

@Serializable
data class PostgresConfiguration(
    val jdbcUrl: String,
    val username: String,
    val password: String,
    val schema: String? = null
)
