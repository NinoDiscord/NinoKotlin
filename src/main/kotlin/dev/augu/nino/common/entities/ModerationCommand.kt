package dev.augu.nino.common.entities

import dev.augu.nino.butterfly.command.Command

abstract class ModerationCommand(name: String, description: String, vararg aliases: String, userPermissions: Long = 0, botPermissions: Long = 0)
    : Command(
        name,
        "moderation",
        *aliases,
        description = description,
        userPermissions = userPermissions,
        botPermissions = botPermissions,
        guildOnly = true)
