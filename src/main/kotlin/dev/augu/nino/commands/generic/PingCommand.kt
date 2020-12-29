package dev.augu.nino.commands.generic

import dev.augu.nino.butterfly.command.CommandContext
import dev.augu.nino.butterfly.util.edit
import dev.augu.nino.common.entities.GenericCommand
import dev.augu.nino.services.discord.IDiscordService
import java.time.temporal.ChronoUnit

class PingCommand(private val discordService: IDiscordService): GenericCommand(
        "ping",
        "Shows the bot's latency with Discord",
        "pong", "pang"
) {
    override suspend fun execute(ctx: CommandContext) {
        val startedAt = ctx.message.timeCreated.toInstant()
        val msg = ctx.replyTranslate("pingCommandOldMessage")

        val gatewayPing = discordService.gatewayPing
        msg.edit(ctx.language()!!.translate("pingCommandNewMessage", mapOf(
                "id" to "0", // it's not sharded as of now anyways
                "shard" to gatewayPing.toString(),
                "messageLatency" to startedAt.until(msg.timeCreated.toInstant(), ChronoUnit.MILLIS).toString()
        )))
    }
}
