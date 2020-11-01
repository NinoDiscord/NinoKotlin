package dev.augu.nino.commands.generic

import dev.augu.nino.butterfly.command.CommandContext
import dev.augu.nino.common.entities.GenericCommand
import dev.augu.nino.common.humanize

import java.lang.management.ManagementFactory

class UptimeCommand: GenericCommand(
        "uptime",
        "Shows the current uptime of the bot.",
        "up", "time"
) {
    override suspend fun execute(ctx: CommandContext) {
        ctx.reply(":gear: **${ManagementFactory.getRuntimeMXBean().uptime.humanize()}**")
    }
}
