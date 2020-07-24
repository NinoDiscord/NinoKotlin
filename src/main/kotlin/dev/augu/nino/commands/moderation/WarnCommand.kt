package dev.augu.nino.commands.moderation

import dev.augu.nino.butterfly.command.CommandContext

class WarnCommand : ModerationCommand("warn", "Warns the user.") {
    override suspend fun execute(ctx: CommandContext) {
        ctx.reply("Boo!")
    }
}