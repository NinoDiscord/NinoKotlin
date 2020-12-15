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
        jda.on<MessageBulkDeleteEvent>().subscribe(this::onMessageDeleteBulk)
        jda.on<MessageDeleteEvent>().subscribe(this::onMessageDelete)
        jda.on<MessageUpdateEvent>().subscribe(this::onMessageUpdate)
    }

    override fun onMessageDeleteBulk(event: MessageBulkDeleteEvent) {
        logger.debug("some event that is named onMessageDeleteBulk?")
    }

    override fun onMessageDelete(event: MessageDeleteEvent) {
        logger.debug("some event that is named onMessageDelete?")
    }

    override fun onMessageUpdate(event: MessageUpdateEvent) {
        logger.info("received updated message: ${event.message.contentRaw}")
    }
}
