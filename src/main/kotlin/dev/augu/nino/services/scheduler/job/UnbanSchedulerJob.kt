package dev.augu.nino.services.scheduler.job

import dev.augu.nino.common.entities.Action
import dev.augu.nino.services.cases.ICaseService
import dev.augu.nino.services.moderation.IModerationService
import dev.augu.nino.services.moderation.log.IModerationLogService
import java.time.Instant
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.dv8tion.jda.api.JDA
import org.koin.core.context.KoinContextHandler
import org.litote.kmongo.Id
import org.litote.kmongo.newId

@Serializable
class UnbanSchedulerJob(
    @Contextual @SerialName("_id") override val id: Id<SchedulerJob> = newId(),
    @Contextual override val startTime: Instant = Instant.now(),
    override val duration: Long,
    override val targetUserId: String,
    override val guildId: String,
    val reason: String?
) : SchedulerJob {
    override val action: Action = Action.UNBAN

    override suspend fun processJob() {
        val koin = KoinContextHandler.get()
        val jda = koin.get<JDA>()
        val guild = jda.getGuildById(guildId) ?: return

        val moderationService = koin.get<IModerationService>()

        moderationService.unban(targetUserId, guild, reason)

        val caseService = koin.get<ICaseService>()
        val moderationLogService = koin.get<IModerationLogService>()

        val resolvingCase = caseService.findLastCaseByActionAndUser(guildId, Action.BAN, targetUserId)
        if (resolvingCase != null) {
            caseService.resolveCase(resolvingCase)
            moderationLogService.updateLog(resolvingCase)
        }

        val case = caseService.createUnbanCase(targetUserId, jda.selfUser.id, null, Instant.now(), null, guildId, false, reason, null)
        moderationLogService.log(case)
    }
}
