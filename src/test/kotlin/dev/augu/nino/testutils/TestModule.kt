package dev.augu.nino.testutils

import dev.augu.nino.butterfly.ButterflyClient
import dev.augu.nino.common.entities.Locale
import dev.augu.nino.configuration.BaseConfiguration
import dev.augu.nino.configuration.Configuration
import dev.augu.nino.configuration.MongoConfiguration
import dev.augu.nino.configuration.PostgresConfiguration
import dev.augu.nino.configuration.RedisConfiguration
import dev.augu.nino.services.baseServiceModule
import org.koin.dsl.module

val mockDiscordModule = module {
    single { ButterflyClient(get(), arrayOf("239790360728043520", "280158289667555328")) }
}

val mockConfigurationModule = module {
    single {
        Configuration(
            base = BaseConfiguration("token", listOf("239790360728043520", "280158289667555328"), "en_US", listOf("test!")),
            redis = RedisConfiguration(),
            postgres = PostgresConfiguration("", "", ""),
            mongodb = MongoConfiguration("", "")
        )
    }
}

val mockLocaleModule = module {
    single {
        listOf<Locale>()
    }
}

val testModule = mockDiscordModule + mockLocaleModule + mockConfigurationModule + baseServiceModule
