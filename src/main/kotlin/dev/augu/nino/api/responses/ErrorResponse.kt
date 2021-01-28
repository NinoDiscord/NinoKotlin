package dev.augu.nino.api.responses

import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponse(
    val message: String
)
