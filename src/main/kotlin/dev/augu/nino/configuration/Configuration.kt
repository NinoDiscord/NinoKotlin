package dev.augu.nino.configuration

import kotlinx.serialization.Serializable

@Serializable
data class Configuration(
    val base: BaseConfiguration,
    val redis: RedisConfiguration,
    val postgres: PostgresConfiguration,
    val mongodb: MongoConfiguration,
    val status: StatusConfig? = null
)
