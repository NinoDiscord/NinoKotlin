package dev.augu.nino.services.settings

import dev.augu.nino.common.entities.database.GuildGeneralSettings
import net.dv8tion.jda.api.entities.Role
import net.dv8tion.jda.api.entities.TextChannel

interface IGuildSettingsService {
    fun getGuildGeneralSettings(guildId: String): GuildGeneralSettings

    fun getMutedRole(guildId: String): Role?

    fun setMutedRole(roleId: String, guildId: String)
}
