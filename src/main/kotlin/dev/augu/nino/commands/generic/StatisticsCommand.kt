package dev.augu.nino.commands.generic

import dev.augu.nino.butterfly.command.CommandContext
import dev.augu.nino.common.entities.GenericCommand
import dev.augu.nino.common.formatBytes
import dev.augu.nino.common.humanize
import dev.augu.nino.common.util.createEmbed
import java.lang.management.ManagementFactory
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDAInfo

class StatisticsCommand(private val jda: JDA): GenericCommand(
        "statistics",
        "View statistics related to the bot.",
        "stats", "botinfo", "info", "me", "self"
) {
    override suspend fun execute(ctx: CommandContext) {
        val embed = createEmbed {
            setAuthor("${ctx.me.asTag} [v1.0.0 / uwu]", null, ctx.me.avatarUrl)
            addField("❯   Miscellaneous", """
                • Guilds: **${jda.guilds.size}**
                • Users: **${jda.users.size}**
                • Uptime: **${(ManagementFactory.getRuntimeMXBean().uptime).humanize()}**
                • Channels: **${jda.textChannels.size + jda.voiceChannels.size}**
                • Gateway Ping: **${jda.gatewayPing}ms**
                • Java: **${System.getProperty("java.version") ?: "unknown"}**
                • JDA: **${JDAInfo.VERSION}**
                • Kotlin: **${KotlinVersion.CURRENT}**
            """.trimIndent(), true)

            addField("❯   Process [${ManagementFactory.getRuntimeMXBean().pid}]", """
                • Memory Usage: **${(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()).formatBytes()}**
                • Operating System: **${System.getProperty("os.name") ?: "Unknown Hardware"} (${System.getProperty("os.arch") ?: "x64"})**
            """.trimIndent(), true)
        }.build()

        ctx.reply(embed)
    }
}
