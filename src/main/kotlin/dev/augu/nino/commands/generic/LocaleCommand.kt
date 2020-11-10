package dev.augu.nino.commands.generic

import dev.augu.nino.butterfly.command.CommandContext
import dev.augu.nino.common.entities.GenericCommand
import dev.augu.nino.common.util.createEmbed
import dev.augu.nino.services.locale.LocaleService

class LocaleCommand(
        private val locales: LocaleService
): GenericCommand(
        "locale",
        "View, reset, or set the user/guild language.",
        "language", "lang"
) {
    override suspend fun execute(ctx: CommandContext) {
        if (ctx.args.isEmpty()) {
            val embed = createEmbed {
                setTitle("[ Localization ]")
                setDescription("""
                    ```apache
                    Guild Language: en_US
                    User Language : en_US
                    ```
                    
                    • To view the list of languages available, use `x!locale list`
                    • To set a guild or user language, use `x!locale set`
                    • To reset a guild or user language, use `x!locale reset`
                """.trimIndent())
            }

            ctx.reply(embed.build())
            return
        }

        return when (ctx.args[0]) {
            "reset" -> onLocaleReset(ctx)
            "list" -> onLocaleList(ctx)
            "set" -> onLocaleSet(ctx)
            else -> onLocaleList(ctx)
        }
    }

    private suspend fun onLocaleReset(ctx: CommandContext) {
        ctx.reply(":pencil2: **| Reset the guild's locale to en_US.**")
    }

    private suspend fun onLocaleList(ctx: CommandContext) {
        val embed = createEmbed {
            setTitle("[ Languages Available ]")
            setDescription(locales.locales.joinToString(", ") { lang ->
                "• [${lang.flag}] **${lang.name}** by **${lang.contributors[0]}** (${locales.coverage(lang)}%, ${lang.contributors.size} contributors | `x!locale set ${lang.code}`)"
            })
        }

        ctx.reply(embed.build())
    }

    private suspend fun onLocaleSet(ctx: CommandContext) {
        ctx.reply(":white_check_mark: **Set guild locale to ${ctx.args[1]}**")
    }
}
