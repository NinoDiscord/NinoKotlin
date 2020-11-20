package dev.augu.nino.services.scheduler.job

import dev.augu.nino.common.entities.Action
import dev.augu.nino.services.moderation.IModerationService
import java.time.Instant
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.dv8tion.jda.api.JDA
import org.koin.core.context.KoinContextHandler
import org.litote.kmongo.Id
import org.litote.kmongo.newId

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
        val jda = KoinContextHandler.get().get<JDA>()
        val guild = jda.getGuildById(guildId) ?: return

        val moderationService = KoinContextHandler.get().get<IModerationService>()

        moderationService.kick(targetUserId, guild, reason)
    }
}
