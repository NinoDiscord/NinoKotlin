package dev.augu.nino.configuration

import kotlinx.serialization.Serializable

@Serializable
data class BaseConfiguration(
        val token: String,
        val owners: List<String>,
        val defaultLanguage: String,
        val prefixes: List<String>
)