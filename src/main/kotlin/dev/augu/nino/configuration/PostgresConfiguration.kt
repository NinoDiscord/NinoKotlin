package dev.augu.nino.configuration

import kotlinx.serialization.Serializable

@Serializable
data class PostgresConfiguration(
        val host: String,
        val port: Int,
        val username: String,
        val password: String,
        val database: String,
        val schema: String?
)