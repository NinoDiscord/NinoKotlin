package dev.augu.nino

import dev.augu.nino.butterfly.ButterflyClient
import dev.augu.nino.butterfly.command.Command
import dev.augu.nino.butterfly.i18n.I18nLanguage
import dev.augu.nino.commands.commandModules
import dev.augu.nino.common.entities.Locale
import dev.augu.nino.common.modules.commonModules
import dev.augu.nino.common.util.createThread
import dev.augu.nino.configuration.Configuration
import dev.augu.nino.configuration.configurationModule
import dev.augu.nino.services.serviceModule
import org.koin.core.KoinComponent
import org.koin.core.context.startKoin
import org.koin.core.inject
import org.slf4j.LoggerFactory
import java.io.File

class Bot: KoinComponent {
    private val client: ButterflyClient by inject()
    private val config: Configuration by inject()
    private val logger = LoggerFactory.getLogger(javaClass)

    init {
        Thread.currentThread().name = "Nino-MainThread"

        val commands = getKoin().getAll<Command>()
        logger.info("Loading ${commands.size} commands...")
        commands.forEach {
            logger.info("Loaded Command: ${it.name}")
            client.addCommand(it)
        }
        logger.info("Commands Loaded!")

        val locales = getKoin().get<List<Locale>>()
        logger.info("Loading ${locales.size} locales...")
        locales.forEach {
            logger.info("Loaded Locale: ${it.name} ${if (it.code == config.base.defaultLanguage) "(Default)" else ""}")
            client.addLanguage(I18nLanguage(it.name, it.translations))
        }
    }

    /**
     * Starts the bot's process
     */
    fun start() {
        client.jda.awaitReady()
        processEndHook()
    }

    /**
     * Adds a shut-down hook
     */
    private fun processEndHook() {
        logger.info("Added shutdown hook")
        Runtime.getRuntime().addShutdownHook(createThread("Nino-ShutdownThread") {
            logger.info("Now shutting down...")

            client.jda.shutdownNow()
        })
    }
}

object Bootstrap {
    @JvmStatic
    fun main(args: Array<String>) {
        startKoin {
            if (File("/etc/nino.properties").exists()) {
                fileProperties("/etc/nino.properties")
            }

            environmentProperties()
            modules(*commonModules.toTypedArray(), *commandModules.toTypedArray(), serviceModule, configurationModule)
        }

        val bot = Bot()
        bot.start()
    }
}