package dev.augu.nino.services.logging

import club.minnced.jda.reactor.on
import dev.augu.nino.common.util.delegated.logging
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.events.message.MessageBulkDeleteEvent
import net.dv8tion.jda.api.events.message.MessageDeleteEvent
import net.dv8tion.jda.api.events.message.MessageUpdateEvent

class LogEventListenerService(val jda: JDA): ILogEventListenerService {
    private val logger by logging(this::class.java)

    init {
        // TODO: make this cleaner with private properties

        logger.info("test")

        jda.on<MessageDeleteEvent>().subscribe {
            // TODO: cache messages
        }

        jda.on<MessageBulkDeleteEvent>().subscribe {
            // TODO: cache messages
        }

        jda.on<MessageUpdateEvent>().subscribe { event ->
            // TODO: cache messages
            logger.info("received updated message: ${event.message.contentRaw}")
        }
    }
}
