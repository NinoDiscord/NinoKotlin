package dev.augu.nino.commands.generic

import dev.augu.nino.butterfly.ButterflyClient
import dev.augu.nino.butterfly.command.CommandContext
import dev.augu.nino.common.entities.GenericCommand
import dev.augu.nino.common.entities.Locale
import dev.augu.nino.common.util.createEmbed

class LocaleCommand(
        private val locales: List<Locale>,
        private val client: ButterflyClient
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

        val subcommand = ctx.args[0]
        ctx.args.drop(1)

        return when (subcommand) {
            "reset" -> onLocaleReset(ctx)
            "list" -> onLocaleList(ctx)
            "set" -> onLocaleSet(ctx)
            else -> onLocaleList(ctx)
        }
    }

    private suspend fun onLocaleReset(ctx: CommandContext) {
        ctx.reply(":pencil2: **| Resetting locale to en_US...**")
    }

    private suspend fun onLocaleList(ctx: CommandContext) {
        ctx.reply("locale list: `en_US`")
    }

    private suspend fun onLocaleSet(ctx: CommandContext) {
        ctx.reply("heck!")
    }
}
