package dev.augu.nino.services.logging

import net.dv8tion.jda.api.events.message.*

interface ILogEventListenerService {
    fun onMessageDeleteBulk(event: MessageBulkDeleteEvent)
    fun onMessageDelete(event: MessageDeleteEvent)
    fun onMessageUpdate(event: MessageUpdateEvent)
}
