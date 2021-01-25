package dev.augu.nino.components

import club.minnced.jda.reactor.on
import dev.augu.nino.common.util.delegated.logging
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent
import net.dv8tion.jda.api.events.message.MessageBulkDeleteEvent
import net.dv8tion.jda.api.events.message.MessageDeleteEvent
import net.dv8tion.jda.api.events.message.MessageUpdateEvent
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

@KoinApiExtension
class EventLogComponent: KoinComponent {
    private val jda: JDA by inject()
    private val logger by logging(this::class.java)

    private fun onMessageDeleteBulk(event: MessageBulkDeleteEvent) {
        logger.debug("some event that is named onMessageDeleteBulk?")
    }

    private fun onGuildMemberLeave(event: GuildMemberRemoveEvent) {
        logger.debug("member ${event.member?.user?.asTag ?: "Unknown Member#0001"} left!")
    }

    private fun onMessageDelete(event: MessageDeleteEvent) {
        logger.debug("some event that is named onMessageDelete?")
    }

    private fun onMessageUpdate(event: MessageUpdateEvent) {
        logger.info("received updated message: ${event.message.contentRaw}")
    }

    fun start() {
        jda.on<GuildMemberRemoveEvent>().subscribe(this::onGuildMemberLeave)

        jda.on<MessageBulkDeleteEvent>().subscribe(this::onMessageDeleteBulk)
        jda.on<MessageDeleteEvent>().subscribe(this::onMessageDelete)
        jda.on<MessageUpdateEvent>().subscribe(this::onMessageUpdate)
    }
}
