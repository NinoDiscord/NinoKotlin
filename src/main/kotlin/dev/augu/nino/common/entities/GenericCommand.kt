package dev.augu.nino.common.entities

import dev.augu.nino.butterfly.command.Command

abstract class GenericCommand(
    name: String,
    description: String,
    vararg aliases: String,
    userPermissions: Long = 0,
    botPermissions: Long = 0
): Command(
        name,
        "generic",
        *aliases,
        description = description,
        userPermissions = userPermissions,
        botPermissions = botPermissions,
        guildOnly = false
)
