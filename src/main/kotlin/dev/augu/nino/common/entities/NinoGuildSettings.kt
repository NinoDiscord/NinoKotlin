package dev.augu.nino.common.entities

import dev.augu.nino.butterfly.GuildSettings
import dev.augu.nino.services.settings.IGuildSettingsService

class NinoGuildSettings(private val guildId: String, override var prefix: String?, val locale: Locale, private val guildSettingsService: IGuildSettingsService) : GuildSettings(prefix, locale.toLanguage()) {
    val mutedRole by lazy { guildSettingsService.getMutedRole(guildId) }
}
