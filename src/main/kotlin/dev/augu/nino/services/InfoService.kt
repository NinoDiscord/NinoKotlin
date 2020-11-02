package dev.augu.nino.services

import dev.augu.nino.butterfly.ButterflyClient
import java.lang.management.ManagementFactory

class InfoService(private val client: ButterflyClient) {
    val version: String = "1.0.0"
    val commit: String = "2e090e4f"
    val uptime: Long = ManagementFactory.getRuntimeMXBean().uptime
    val guilds: Long = client.jda.guildCache.size()
    val users: Long = client.jda.userCache.size()
}