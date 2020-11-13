package dev.augu.nino.common.modules

import club.minnced.jda.reactor.ReactiveEventManager
import dev.augu.nino.butterfly.ButterflyClient
import dev.augu.nino.butterfly.GuildSettingsLoader
import dev.augu.nino.common.entities.NinoGuildSettings
import dev.augu.nino.configuration.Configuration
import dev.augu.nino.services.locale.ILocaleService
import dev.augu.nino.services.settings.IGuildSettingsService
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.requests.GatewayIntent
import net.dv8tion.jda.api.utils.cache.CacheFlag
import org.koin.dsl.module

private class CustomGuildSettingsLoader(private val guildSettingsService: IGuildSettingsService, private val localeService: ILocaleService) : GuildSettingsLoader<NinoGuildSettings> {
    override suspend fun load(guild: Guild): NinoGuildSettings? {
        val settings = guildSettingsService.getGuildGeneralSettings(guild.id)

        return NinoGuildSettings(
                guild.id,
                settings.prefix,
                localeService.locales.firstOrNull { it.code == settings.localeCode } ?: localeService.defaultLocale,
                guildSettingsService
        )
    }

}

val discordModule = module {
    single {
        val config: Configuration = get()
        JDABuilder
                .createDefault(config.base.token)
                .setEventManager(ReactiveEventManager())
                .setLargeThreshold(50)
                .disableCache(
                        CacheFlag.CLIENT_STATUS,
                        CacheFlag.ACTIVITY,
                        CacheFlag.EMOTE,
                        CacheFlag.VOICE_STATE
                )
                .enableIntents(
                        GatewayIntent.GUILD_BANS,
                        GatewayIntent.GUILD_MEMBERS,
                        GatewayIntent.GUILD_MESSAGES
                )
                .disableIntents(
                        GatewayIntent.DIRECT_MESSAGES,
                        GatewayIntent.DIRECT_MESSAGE_REACTIONS,
                        GatewayIntent.DIRECT_MESSAGE_TYPING,
                        GatewayIntent.GUILD_EMOJIS,
                        GatewayIntent.GUILD_INVITES,
                        GatewayIntent.GUILD_MESSAGE_REACTIONS,
                        GatewayIntent.GUILD_MESSAGE_TYPING,
                        GatewayIntent.GUILD_PRESENCES,
                        GatewayIntent.GUILD_VOICE_STATES
                )
                .build()
    }
    single {
        val localeService: ILocaleService = get()
        val guildSettingsService: IGuildSettingsService = get()
        val config: Configuration = get()
        val client = ButterflyClient(
                get(), // JDA
                config.base.owners.toTypedArray(),
                defaultLanguage = localeService.defaultLocale.toLanguage(),
                guildSettingsLoader = CustomGuildSettingsLoader(guildSettingsService, localeService)
        )
        val prefixes = config.base.prefixes
        client.addPrefix(prefixes.first(), *prefixes.drop(1).toTypedArray())
        client
    }
}