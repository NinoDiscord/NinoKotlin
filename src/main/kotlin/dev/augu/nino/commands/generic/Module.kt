package dev.augu.nino.commands.generic

import dev.augu.nino.butterfly.command.Command
import org.koin.dsl.bind
import org.koin.dsl.module

val genericCommandModule = module {
    single { PingCommand(get()) } bind Command::class
    single { LocaleCommand(get()) } bind Command::class
    single { HelpCommand(get(), get()) } bind Command::class
    single { UptimeCommand() } bind Command::class
    single { StatisticsCommand(get()) } bind Command::class
}
