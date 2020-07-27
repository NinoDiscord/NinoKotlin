package dev.augu.nino.common.modules

import dev.augu.nino.butterfly.ButterflyClient
import dev.augu.nino.common.entities.Locale
import dev.augu.nino.configuration.Configuration
import net.dv8tion.jda.api.JDABuilder
import org.koin.dsl.module

val discordModule = module {
    single {
        val config: Configuration = get()
        JDABuilder.createDefault(config.base.token).build()
    }
    single {
        val locales: List<Locale> = get()
        val config: Configuration = get()
        val client = ButterflyClient(
                get(), // JDA
                config.base.owners.toTypedArray(),
                defaultLanguage = locales
                        .filter { it.code == config.base.defaultLanguage }
                        .map { it.toLanguage() }
                        .firstOrNull() // default language
        )
        val prefixes = config.base.prefixes
        client.addPrefix(prefixes.first(), *prefixes.drop(1).toTypedArray())
        client
    }
}