package dev.augu.nino.configuration

import kotlinx.serialization.Serializable

@Serializable
data class MongoConfiguration(val connectionString: String, val database: String)