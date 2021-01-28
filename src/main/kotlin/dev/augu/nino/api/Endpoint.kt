package dev.augu.nino.api

import io.ktor.application.*
import io.ktor.http.*

abstract class Endpoint(
    val method: HttpMethod,
    val path: String
) {

    /**
     * Runs the endpoint
     * @param call The API call request
     */
    abstract suspend fun run(call: ApplicationCall)
}
