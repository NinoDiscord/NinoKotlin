package dev.augu.nino.services.scheduler.job

import dev.augu.nino.common.entities.Action
import java.time.Duration
import java.time.Instant
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import org.litote.kmongo.Id

interface SchedulerJob {
    @Contextual @SerialName("_id") val id: Id<SchedulerJob>
    val action: Action
    @Contextual val startTime: Instant
    val duration: Long
    val targetUserId: String
    val guildId: String

    fun shouldBePersisted(): Boolean {
        return duration >= Duration.ofMinutes(15).toMillis()
    }

    suspend fun processJob()
}
