package dev.augu.nino.commands.generic

import dev.augu.nino.butterfly.command.CommandContext
import dev.augu.nino.common.entities.GenericCommand
import dev.augu.nino.common.util.createEmbed
import dev.augu.nino.common.humanize
import dev.augu.nino.services.InfoService
import net.dv8tion.jda.api.JDAInfo
import java.lang.management.ManagementFactory
import kotlin.KotlinVersion

class StatisticsCommand(private val infos: InfoService): GenericCommand(
        "statistics",
        "View statistics related to the bot.",
        "stats", "botinfo", "info", "me", "self"
) {
    override suspend fun execute(ctx: CommandContext) {
        val embed = createEmbed {
            setAuthor("${ctx.me.asTag} [${infos.version} / ${infos.commit}]", null, ctx.me.avatarUrl)
            addField("❯   Miscellaneous", """
                • Guilds: **${infos.guilds}**
                • Users: **${infos.users}**
                • Uptime: **${infos.uptime.humanize()}**
            """.trimIndent(), true)

            addField("❯   Process [${ManagementFactory.getRuntimeMXBean().pid}]", """
                • Java: **${System.getProperty("java.version") ?: "unknown"}**
                • JDA: **${JDAInfo.VERSION}**
                • Kotlin: **${KotlinVersion.CURRENT}**
            """.trimIndent(), true)
        }.build()

        ctx.reply(embed)
    }
}
