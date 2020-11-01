package dev.augu.nino.commands.generic

import dev.augu.nino.butterfly.command.CommandContext
import dev.augu.nino.common.entities.GenericCommand
import dev.augu.nino.common.entities.Locale

class LocaleCommand(private val locales: List<Locale>): GenericCommand(
        "locale",
        "View, reset, or set the user/guild language.",
        "language", "lang"
) {
    override suspend fun execute(ctx: CommandContext) {
        ctx.reply("locale list here >w>")
    }
}
