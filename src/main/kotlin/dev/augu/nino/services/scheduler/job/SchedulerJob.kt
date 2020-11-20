package dev.augu.nino.services.scheduler.job

import dev.augu.nino.butterfly.ButterflyClient
import dev.augu.nino.common.entities.Action
import org.litote.kmongo.Id
import java.time.Duration

interface SchedulerJob {
    var _id: Id<SchedulerJob>
    val action: Action
    var startTime: Long
    var duration: Long
    val targetUserId: String
    val guildId: String

    fun shouldBePersisted(): Boolean {
        return duration > Duration.ofMinutes(15).toMillis()
    }

    suspend fun processJob(butterflyClient: ButterflyClient)
}
