package dev.augu.nino.commands.generic

import dev.augu.nino.butterfly.command.Command
import dev.augu.nino.butterfly.command.CommandContext
import dev.augu.nino.butterfly.util.edit

class PingCommand : Command("ping", "generic", "pong", "pang", description = "Shows you the bot's ping.", guildOnly = true) {
    override suspend fun execute(ctx: CommandContext) {
        val startedAt = ctx.message.timeCreated
        val msg = ctx.replyTranslate("pingCommandOldMessage")

        val gatewayPing = ctx.client.gatewayPing
        msg.edit(ctx.language()!!.translate("pingCommandMessage", mapOf(
                "id" to ctx.client.jda.shardInfo.shardId.toString(),
                "shard" to gatewayPing.toString(),
                "messageLatency" to (msg.timeCreated.toInstant().toEpochMilli() - startedAt.toInstant().toEpochMilli()).toString()
        )))
    }
}