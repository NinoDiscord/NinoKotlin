package dev.augu.nino.services.scheduler

import dev.augu.nino.butterfly.ButterflyClient
import dev.augu.nino.common.entities.Action
import dev.augu.nino.services.mongodb.IMongoService
import dev.augu.nino.services.scheduler.job.SchedulerJob
import dev.augu.nino.services.scheduler.persistance.MongoPersistor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.launch
import org.litote.kmongo.newId
import java.time.Instant
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.Executors
import kotlin.concurrent.schedule

class SchedulerService(mongoService: IMongoService, private val butterflyClient: ButterflyClient) : ISchedulerService {
    private val mongoPersistor = MongoPersistor(mongoService)
    private val timer = Timer("Scheduler-Timer-Thread")
    private val executionThreadPool = Executors.newFixedThreadPool(3)
    private val scheduledJobs: ConcurrentHashMap<String, TimerTask> = ConcurrentHashMap()
    private val scheduledJobsIds: ConcurrentHashMap<String, String> = ConcurrentHashMap()

    private fun specialKey(action: Action, targetUserId: String, guildId: String): String = "${action.name}:$targetUserId:$guildId"

    override suspend fun scheduleJob(schedulerJob: SchedulerJob) {
        scheduledJobs[schedulerJob._id.toString()] = timer.schedule(schedulerJob.duration) { processJob(schedulerJob, butterflyClient) }
        scheduledJobsIds[specialKey(schedulerJob.action, schedulerJob.targetUserId, schedulerJob.guildId)]

        if (schedulerJob.shouldBePersisted()) {
            mongoPersistor.persistSchedulerJob(schedulerJob)
        }
    }

    override suspend fun stopScheduledJob(action: Action, targetUserId: String, guildId: String) {
        stopScheduledJob(scheduledJobsIds[specialKey(action, targetUserId, guildId)] ?: return)
    }

    override suspend fun stopScheduledJob(schedulerJobId: String) {
        scheduledJobs[schedulerJobId]?.cancel()
        mongoPersistor.completeSchedulerJob(schedulerJobId)
    }

    override suspend fun loadAndScheduleAllJobs() {
        for (schedulerJob in mongoPersistor.getAllScheduledJobs()) {
            mongoPersistor.completeSchedulerJob(schedulerJob)
            val newStartTime = Instant.now().toEpochMilli()
            val newDuration = schedulerJob.duration + schedulerJob.startTime - newStartTime
            schedulerJob.startTime = newStartTime
            schedulerJob.duration = newDuration
            schedulerJob._id = newId()
            scheduleJob(schedulerJob)
        }
    }

    private fun processJob(schedulerJob: SchedulerJob, butterflyClient: ButterflyClient) {
        CoroutineScope(executionThreadPool.asCoroutineDispatcher()).launch {
            schedulerJob.processJob(butterflyClient)

            if (schedulerJob.shouldBePersisted()) {
                mongoPersistor.completeSchedulerJob(schedulerJob)
            }
        }
    }
}
