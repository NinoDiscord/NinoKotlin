package dev.augu.nino.commands.generic

import dev.augu.nino.butterfly.command.Command
import org.koin.dsl.bind
import org.koin.dsl.module

val genericCommandModule = module {
    single { PingCommand() } bind Command::class
    single { HelpCommand(get()) } bind Command::class
}