package dev.augu.nino.services.scheduler.job

import dev.augu.nino.butterfly.ButterflyClient
import dev.augu.nino.common.entities.Action
import java.time.Duration
import org.litote.kmongo.Id

interface SchedulerJob {
    var id: Id<SchedulerJob>
    val action: Action
    var startTime: Long
    var duration: Long
    val targetUserId: String
    val guildId: String

    fun shouldBePersisted(): Boolean {
        return duration >= Duration.ofMinutes(15).toMillis()
    }

    suspend fun processJob(butterflyClient: ButterflyClient)
}
