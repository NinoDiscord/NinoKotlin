package dev.augu.nino.commands.moderation

import dev.augu.nino.butterfly.command.Command
import org.koin.dsl.bind
import org.koin.dsl.module

val moderationModule = module {
    single { WarnCommand() } bind Command::class
}