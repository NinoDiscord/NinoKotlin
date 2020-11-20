package dev.augu.nino.configuration

import kotlinx.serialization.Serializable

@Serializable
data class RedisConfiguration(
    val url: String? = null, // connect via url (redis://password@host:port/database)
        // or construct url automatically
    val host: String? = null,
    val port: Int? = null,
    val password: String? = null,
    val database: Int? = null
)
