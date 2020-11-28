package dev.augu.nino.common.entities

import dev.augu.nino.butterfly.command.Command
import net.dv8tion.jda.api.Permission

abstract class SettingsCommand(name: String, description: String, vararg aliases: String, botPermissions: Long = 0L): Command(
        name,
        "settings",
        *aliases,
        description = description,
        userPermissions = Permission.getRaw(Permission.MANAGE_SERVER),
        botPermissions = botPermissions
)
