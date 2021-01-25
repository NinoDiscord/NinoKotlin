package dev.augu.nino.components

import club.minnced.jda.reactor.on
import dev.augu.nino.butterfly.ButterflyClient
import dev.augu.nino.butterfly.command.Command
import dev.augu.nino.butterfly.i18n.I18nLanguage
import dev.augu.nino.common.entities.Locale
import dev.augu.nino.common.util.createThread
import dev.augu.nino.configuration.Configuration
import dev.augu.nino.services.scheduler.ISchedulerService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.launch
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.OnlineStatus
import net.dv8tion.jda.api.entities.Activity
import net.dv8tion.jda.api.events.DisconnectEvent
import net.dv8tion.jda.api.events.ReadyEvent
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.slf4j.LoggerFactory
import java.util.concurrent.Executors

@KoinApiExtension
class BotComponent: KoinComponent {
    private val client: ButterflyClient by inject()
    private val config: Configuration by inject()
    private val schedulerService: ISchedulerService by inject()
    private val logger = LoggerFactory.getLogger(javaClass)
    private val jda: JDA by inject()

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

        jda.on<ReadyEvent>().subscribe { event ->
            logger.info("Logged in as ${event.jda.selfUser.asTag} | ${event.guildTotalCount} Guilds (${event.guildUnavailableCount} unavailable)")
            logger.info("Setting presence to \"${config.status?.status ?: "default"}\"")
            event.jda.presence.setPresence(
                OnlineStatus.ONLINE,
                Activity.of(
                    Activity.ActivityType.fromKey(config.status?.type ?: 0),
                    config.status?.status ?: "${config.base.prefixes[0]}help | ${event.guildTotalCount} Guilds"
                )
            )
            logger.info("Loading scheduled jobs in the background...")
            CoroutineScope(Executors.newSingleThreadExecutor().asCoroutineDispatcher()).launch {
                schedulerService.loadAndScheduleAllJobs()
            }
        }

        jda.on<DisconnectEvent>().subscribe { event ->
            logger.warn("We have disconnected from Discord once more, now we wait for a new connection?")
            if (event.closeCode != null) {
                logger.error("[${event.closeCode!!.code}]: ${event.closeCode!!.meaning}")
            }
        }
    }

    /**
     * Starts the bots process
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
