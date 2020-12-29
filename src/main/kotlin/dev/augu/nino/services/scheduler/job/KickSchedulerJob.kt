package dev.augu.nino.services.scheduler.job

import dev.augu.nino.common.entities.Action
import dev.augu.nino.services.cases.ICaseService
import dev.augu.nino.services.discord.IDiscordService
import dev.augu.nino.services.moderation.IModerationService
import dev.augu.nino.services.moderation.log.IModerationLogService
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.koin.core.context.KoinContextHandler
import org.litote.kmongo.Id
import org.litote.kmongo.newId
import java.time.Instant

@Serializable
class KickSchedulerJob(
    @Contextual @SerialName("_id") override val id: Id<SchedulerJob> = newId(),
    @Contextual override val startTime: Instant = Instant.now(),
    override val duration: Long,
    override val targetUserId: String,
    override val guildId: String,
    val reason: String?
) : SchedulerJob {
    override val action: Action = Action.KICK

    override suspend fun processJob() {
        val koin = KoinContextHandler.get()
        val discordService = koin.get<IDiscordService>()
        val guild = discordService.extractGuildFromId(guildId) ?: return

        val moderationService = koin.get<IModerationService>()

        moderationService.kick(targetUserId, guild, reason)

        val case = koin.get<ICaseService>().createKickCase(targetUserId, discordService.selfUser.id, null, Instant.now(), null, guildId, false, reason, null)
        koin.get<IModerationLogService>().log(case)
    }
}
