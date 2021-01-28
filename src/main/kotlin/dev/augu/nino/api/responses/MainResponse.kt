package dev.augu.nino.api.responses

import kotlinx.serialization.Serializable

@Serializable
data class MainResponse(
    val hello: String,
    val docs: String
)
