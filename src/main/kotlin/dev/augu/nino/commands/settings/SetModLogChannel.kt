package dev.augu.nino.commands.settings

import dev.augu.nino.butterfly.command.CommandContext
import dev.augu.nino.common.entities.SettingsCommand

class SetModLogChannel: SettingsCommand(
        "set-modlog",
        "Sets the mod log channel",
        "modlog"
) {

    override suspend fun execute(ctx: CommandContext) {
        ctx.reply("sup")
    }
}
