package dev.augu.nino.commands.moderation

import dev.augu.nino.butterfly.command.Command
import org.koin.dsl.bind
import org.koin.dsl.module

val moderationModule = module {
    single { WarnCommand() } bind Command::class
    single { BanCommand(get(), get()) } bind Command::class
    single { UnbanCommand(get(), get()) } bind Command::class
    single { SoftbanCommand(get(), get()) } bind Command::class
    single { KickCommand(get(), get()) } bind Command::class
    single { MuteCommand(get(), get(), get()) } bind Command::class
    single { UnmuteCommand(get(), get(), get()) } bind Command::class
}