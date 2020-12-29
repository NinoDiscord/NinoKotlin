package dev.augu.nino.services

import dev.augu.nino.services.cases.CaseService
import dev.augu.nino.services.cases.ICaseService
import dev.augu.nino.services.discord.DiscordService
import dev.augu.nino.services.discord.IDiscordService
import dev.augu.nino.services.locale.ILocaleService
import dev.augu.nino.services.locale.LocaleService
import dev.augu.nino.services.logging.LogEventListenerService
import dev.augu.nino.services.moderation.IModerationService
import dev.augu.nino.services.moderation.ModerationService
import dev.augu.nino.services.moderation.log.IModerationLogService
import dev.augu.nino.services.moderation.log.ModerationLogService
import dev.augu.nino.services.mongodb.IMongoService
import dev.augu.nino.services.mongodb.MongoService
import dev.augu.nino.services.postgres.IPostgresService
import dev.augu.nino.services.postgres.PostgresService
import dev.augu.nino.services.redis.IRedisService
import dev.augu.nino.services.redis.RedisService
import dev.augu.nino.services.scheduler.ISchedulerService
import dev.augu.nino.services.scheduler.SchedulerService
import dev.augu.nino.services.settings.GuildSettingsService
import dev.augu.nino.services.settings.IGuildSettingsService
import org.koin.dsl.module

val discordServiceModule = module {
    single<IDiscordService> { DiscordService(get()) }
}

val baseServiceModule = module {
    single<ILocaleService> { LocaleService(get(), get()) }
    single<IRedisService> { RedisService(get()) }
    single<IPostgresService> { PostgresService(get()) }
    single<IModerationService> { ModerationService(get(), get(), get()) }
    single<IGuildSettingsService> { GuildSettingsService(get(), get(), get()) }
    single<IMongoService> { MongoService(get()) }
    single<ISchedulerService> { SchedulerService(get()) }
    single<ICaseService> { CaseService(get(), get()) }
    single<IModerationLogService> { ModerationLogService(get(), get()) }
    single { LogEventListenerService(get()) }
}

val serviceModule = baseServiceModule + discordServiceModule
