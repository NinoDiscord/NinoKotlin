package dev.augu.nino.commands.settings

import org.koin.dsl.module

val settingsModule = module {
    single { SetModLogChannel() }
}
