package dev.augu.nino.commands.generic

import dev.augu.nino.butterfly.command.CommandContext
import dev.augu.nino.services.InfoService

class InfoCommand(private val infoService: InfoService)
    : GenericCommand("info", "Shows more information about the bot.", "botinfo", "bot") {

    override suspend fun execute(ctx: CommandContext) {
        ctx.reply("Guild Count: " + infoService.guildCount())
    }
}