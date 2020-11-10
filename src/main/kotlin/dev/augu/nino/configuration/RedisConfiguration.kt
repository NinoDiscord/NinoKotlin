package dev.augu.nino.configuration

import kotlinx.serialization.Serializable

@Serializable
data class RedisConfiguration(
        val url: String?, // connect via url (redis://password@host:port/database)
        // or construct url automatically
        val host: String?,
        val port: Int?,
        val password: String?,
        val database: Int?
)