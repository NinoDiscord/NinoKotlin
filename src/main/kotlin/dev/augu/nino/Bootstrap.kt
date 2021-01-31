package dev.augu.nino

import dev.augu.nino.components.APIComponent
import dev.augu.nino.components.BotComponent
import dev.augu.nino.components.EventLogComponent
import org.koin.core.component.KoinApiExtension

object Bootstrap {
    @KoinApiExtension
    @JvmStatic
    fun main(args: Array<String>) {
        val api = APIComponent()
        api.start()

        val bot = BotComponent()
        bot.start()

        val eventLog = EventLogComponent()
        eventLog.start()
    }
}
