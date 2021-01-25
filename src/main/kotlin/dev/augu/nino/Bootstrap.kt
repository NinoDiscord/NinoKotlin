package dev.augu.nino

import dev.augu.nino.commands.commandModules
import dev.augu.nino.common.modules.commonModules
import dev.augu.nino.components.BotComponent
import dev.augu.nino.configuration.configurationModule
import dev.augu.nino.services.serviceModule
import org.koin.core.component.KoinApiExtension
import org.koin.core.context.startKoin
import java.io.File

object Bootstrap {
    @KoinApiExtension
    @JvmStatic
    fun main(args: Array<String>) {
        startKoin {
            if (File("/etc/nino.properties").exists()) {
                fileProperties("/etc/nino.properties")
            }

            environmentProperties()
            modules(*(commonModules + commandModules + serviceModule + configurationModule).toTypedArray())
        }

        val bot = BotComponent()
        bot.start()
    }
}
