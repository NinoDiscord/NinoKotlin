package dev.augu.nino.services

import dev.augu.nino.butterfly.ButterflyClient

class InfoService(private val butterflyClient: ButterflyClient) {

    fun guildCount(): Int = butterflyClient.guilds.size

}