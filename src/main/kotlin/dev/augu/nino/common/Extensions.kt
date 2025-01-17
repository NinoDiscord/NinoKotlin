package dev.augu.nino.common

import dev.augu.nino.api.Endpoint
import dev.augu.nino.api.endpoints.endpointsModule
import dev.augu.nino.commands.commandModules
import dev.augu.nino.common.modules.commonModules
import dev.augu.nino.configuration.configurationModule
import dev.augu.nino.services.serviceModule
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.routing.*
import io.ktor.serialization.*
import org.koin.ktor.ext.Koin
import java.io.File

/**
 * Returns the humanized time for a [java.lang.Long] instance
 * @credit // Credit: https://github.com/DV8FromTheWorld/Yui/blob/master/src/main/java/net/dv8tion/discord/commands/UptimeCommand.java#L34
 */
fun Long.humanize(): String {
    val months = this / 2592000000L % 12
    val weeks = this / 604800000L % 7
    val days = this / 86400000L % 30
    val hours = this / 3600000L % 24
    val minutes = this / 60000L % 60
    val seconds = this / 1000L % 60

    val str = StringBuilder()
    if (months > 0) str.append("${months}mo")
    if (weeks > 0) str.append("${weeks}w")
    if (days > 0) str.append("${days}d")
    if (hours > 0) str.append("${hours}h")
    if (minutes > 0) str.append("${minutes}m")
    if (seconds > 0) str.append("${seconds}s")

    return str.toString()
}

/**
 * Returns a readable format for memory
 */
fun Long.formatBytes(): String {
    val kb = this / 1024L
    val mb = kb / 1024L
    val gb = mb / 1024L

    return if (kb < 1024L) "${kb}KB"
        else if (kb > 1024L && mb < 1024L) "${mb}MB"
        else "${gb}GB"
}

fun Application.module() {
    // Modules are already registered, so?
    install(Koin) {
        if (File("/etc/nino.properties").exists()) fileProperties("/etc/nino.properties")

        environmentProperties()
        modules(*(commonModules + commandModules + serviceModule + configurationModule + endpointsModule).toTypedArray())

        val endpoints = koin.getAll<Endpoint>()
        install(Routing) {
            for (endpoint in endpoints) {
                route(endpoint.path, endpoint.method) {
                    handle { endpoint.run(call) }
                }
            }
        }
    }

    install(ContentNegotiation) {
        json(contentType = ContentType.parse("application/json"))
    }

    install(DefaultHeaders) {
        header("X-Powered-By", "Nino (https://github.com/NinoDiscord/Nino)")
    }

    install(CORS) {
        header(HttpHeaders.XForwardedProto)
        anyHost()
    }
}
