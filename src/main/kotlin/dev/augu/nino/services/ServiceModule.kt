package dev.augu.nino.services

import org.koin.dsl.module

val serviceModule = module {
    single { InfoService(get()) }
    single { LocaleService(get(), get()) }
}