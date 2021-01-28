package dev.augu.nino.components

import dev.augu.nino.common.module
import dev.augu.nino.common.util.delegated.logging
import dev.augu.nino.configuration.Configuration
import dev.augu.nino.configuration.Environment
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.slf4j.LoggerFactory

@KoinApiExtension
class APIComponent: KoinComponent {
    private val config: Configuration by inject()
    private val logger by logging(this::class.java)

    fun start() {
        if (config.api == null) {
            logger.warn("Internal API is not enabled, skipping")
            return
        }

        logger.info("Starting internal API...")
        val apiConfig = config.api!! // keep a hard copy
        val environment = applicationEngineEnvironment {
            this.developmentMode = this@APIComponent.config.environment == Environment.Development
            this.log = LoggerFactory.getLogger("dev.augu.nino.ktor.Application")

            connector {
                host = apiConfig.host ?: "0.0.0.0"
                port = apiConfig.port
            }

            module { module() }
        }

        val server = embeddedServer(Netty, environment)
        server.start(wait = true)

        logger.info("Started API server at 'http://${apiConfig.host ?: "0.0.0.0"}:${apiConfig.port}'")
    }
}
