package dev.augu.nino.services.scheduler.job

import dev.augu.nino.butterfly.ButterflyClient
import dev.augu.nino.common.entities.Action
import dev.augu.nino.services.moderation.IModerationService
import java.time.Instant
import kotlinx.serialization.Serializable
import org.koin.core.context.KoinContextHandler
import org.litote.kmongo.Id
import org.litote.kmongo.newId

@Serializable
data class BanSchedulerJob(
    override var id: Id<SchedulerJob> = newId(),
    override var startTime: Long = Instant.now().toEpochMilli(),
    override var duration: Long,
    override val targetUserId: String,
    override val guildId: String,
    val delDays: Int,
    val reason: String?
) : SchedulerJob {
    override val action: Action = Action.BAN

    override suspend fun processJob(butterflyClient: ButterflyClient) {
        val jda = butterflyClient.jda
        val guild = jda.getGuildById(guildId) ?: return

        val moderationService = KoinContextHandler.get().get<IModerationService>()

        moderationService.ban(targetUserId, guild, reason, delDays)
    }
}
