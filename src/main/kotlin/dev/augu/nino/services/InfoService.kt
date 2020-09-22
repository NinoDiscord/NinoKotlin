package dev.augu.nino.services

import dev.augu.nino.butterfly.ButterflyClient

class InfoService(private val client: ButterflyClient) {
    fun guildCount(): Int = client.guilds.size
}