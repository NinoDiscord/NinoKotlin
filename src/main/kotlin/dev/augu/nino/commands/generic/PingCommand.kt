package dev.augu.nino.commands.generic

import dev.augu.nino.butterfly.command.CommandContext
import dev.augu.nino.common.entities.GenericCommand

class PingCommand: GenericCommand(
        "ping",
        "Shows the bot's ping.",
        "pong", "pang"
) {
    override suspend fun execute(ctx: CommandContext) {
        val startedAt = ctx.message.timeCreated
        val msg = ctx.replyTranslate("pingCommandOldMessage")

        val gatewayPing = ctx.client.jda.gatewayPing
        msg.editMessage(ctx.language()!!.translate("pingCommandNewMessage", mapOf(
                "id" to ctx.client.jda.shardInfo.shardId.toString(),
                "shard" to gatewayPing.toString(),
                "messageLatency" to (msg.timeCreated.toInstant().toEpochMilli() - startedAt.toInstant().toEpochMilli()).toString()
        )))
    }
}