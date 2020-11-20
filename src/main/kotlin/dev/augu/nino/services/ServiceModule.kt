package dev.augu.nino.services

import dev.augu.nino.services.discord.DiscordService
import dev.augu.nino.services.discord.IDiscordService
import dev.augu.nino.services.locale.ILocaleService
import dev.augu.nino.services.locale.LocaleService
import dev.augu.nino.services.moderation.IModerationService
import dev.augu.nino.services.moderation.ModerationService
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

val serviceModule = module {
    single<ILocaleService> { LocaleService(get(), get()) }
    single<IRedisService> { RedisService(get()) }
    single<IPostgresService> { PostgresService(get()) }
    single<IModerationService> { ModerationService(get(), get(), get()) }
    single<IDiscordService> { DiscordService() }
    single<IGuildSettingsService> { GuildSettingsService(get(), get(), get()) }
    single<IMongoService> { MongoService(get()) }
    single<ISchedulerService> { SchedulerService(get(), get()) }
}