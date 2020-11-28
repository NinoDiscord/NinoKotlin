package dev.augu.nino.commands.generic

import dev.augu.nino.butterfly.command.CommandContext
import dev.augu.nino.butterfly.util.edit
import dev.augu.nino.common.entities.GenericCommand
import java.time.temporal.ChronoUnit
import net.dv8tion.jda.api.JDA

class PingCommand(private val jda: JDA): GenericCommand(
        "ping",
        "Shows the bot's latency with Discord",
        "pong", "pang"
) {
    override suspend fun execute(ctx: CommandContext) {
        val startedAt = ctx.message.timeCreated.toInstant()
        val msg = ctx.replyTranslate("pingCommandOldMessage")

        val gatewayPing = jda.gatewayPing
        msg.edit(ctx.language()!!.translate("pingCommandNewMessage", mapOf(
                "id" to jda.shardInfo.shardId.toString(),
                "shard" to gatewayPing.toString(),
                "messageLatency" to startedAt.until(msg.timeCreated.toInstant(), ChronoUnit.MILLIS).toString()
        )))
    }
}
