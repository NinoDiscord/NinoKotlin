package dev.augu.nino.services

import org.koin.dsl.module

val serviceModule = module {
    single { LocaleService(get(), get()) }
}