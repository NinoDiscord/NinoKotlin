package dev.augu.nino.services

import dev.augu.nino.services.locale.LocaleService
import dev.augu.nino.services.postgres.PostgresService
import dev.augu.nino.services.redis.RedisService
import org.koin.dsl.module

val serviceModule = module {
    single { LocaleService(get(), get()) }
    single { RedisService(get()) }
    single { PostgresService(get()) }
}