package dev.augu.nino.services.scheduler.job

import dev.augu.nino.common.entities.Action
import dev.augu.nino.services.cases.ICaseService
import dev.augu.nino.services.discord.IDiscordService
import dev.augu.nino.services.moderation.IModerationService
import dev.augu.nino.services.moderation.log.IModerationLogService
import java.time.Instant
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.koin.core.context.GlobalContext
import org.litote.kmongo.Id
import org.litote.kmongo.newId

@Serializable
data class BanSchedulerJob(
    @Contextual @SerialName("_id") override val id: Id<SchedulerJob> = newId(),
    @Contextual override val startTime: Instant = Instant.now(),
    override val duration: Long,
    override val targetUserId: String,
    override val guildId: String,
    val delDays: Int,
    val reason: String?
) : SchedulerJob {
    override val action: Action = Action.BAN

    override suspend fun processJob() {
        val koin = GlobalContext.get()
        val discordService = koin.get<IDiscordService>()
        val guild = discordService.extractGuildFromId(guildId) ?: return

        val moderationService = koin.get<IModerationService>()

        moderationService.ban(targetUserId, guild, reason, delDays)

        val case = koin.get<ICaseService>().createBanCase(targetUserId, discordService.selfUser.id, null, Instant.now(), null, guildId, false, reason, null, null, false)
        koin.get<IModerationLogService>().log(case)
    }
}
