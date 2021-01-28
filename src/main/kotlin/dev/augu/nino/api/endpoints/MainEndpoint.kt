package dev.augu.nino.api.endpoints

import dev.augu.nino.api.Endpoint
import dev.augu.nino.api.responses.MainResponse
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*

class MainEndpoint: Endpoint(HttpMethod.Get, "/") {
    override suspend fun run(call: ApplicationCall) =
        call.respond(MainResponse("world", "https://nino.augu.dev/docs"))
}
