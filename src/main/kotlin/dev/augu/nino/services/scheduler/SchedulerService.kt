package dev.augu.nino.services.scheduler

import dev.augu.nino.butterfly.ButterflyClient
import dev.augu.nino.common.entities.Action
import dev.augu.nino.services.mongodb.IMongoService
import dev.augu.nino.services.scheduler.job.SchedulerJob
import dev.augu.nino.services.scheduler.persistance.MongoPersistor
import java.time.Instant
import java.util.Timer
import java.util.TimerTask
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.Executors
import kotlin.concurrent.schedule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.launch

class SchedulerService(mongoService: IMongoService) : ISchedulerService {
    private val mongoPersistor = MongoPersistor(mongoService)
    private val timer = Timer("Scheduler-Timer-Thread")
    private val executionThreadPool = Executors.newFixedThreadPool(3)
    private val scheduledJobs: ConcurrentHashMap<String, TimerTask> = ConcurrentHashMap()
    private val scheduledJobsIds: ConcurrentHashMap<String, String> = ConcurrentHashMap()

    private fun specialKey(action: Action, targetUserId: String, guildId: String): String = "${action.name}:$targetUserId:$guildId"

    override suspend fun scheduleJob(schedulerJob: SchedulerJob) {
        scheduledJobs[schedulerJob.id.toString()] = timer.schedule(schedulerJob.duration) { processJob(schedulerJob) }
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
            val newStartTime = Instant.now()
            val newDuration = schedulerJob.duration + schedulerJob.startTime.toEpochMilli() - newStartTime.toEpochMilli()

            scheduledJobs[schedulerJob.id.toString()] = timer.schedule(newDuration) { processJob(schedulerJob) }
            scheduledJobsIds[specialKey(schedulerJob.action, schedulerJob.targetUserId, schedulerJob.guildId)]
        }
    }

    private fun processJob(schedulerJob: SchedulerJob) {
        CoroutineScope(executionThreadPool.asCoroutineDispatcher()).launch {
            schedulerJob.processJob()

            if (schedulerJob.shouldBePersisted()) {
                mongoPersistor.completeSchedulerJob(schedulerJob)
            }
        }
    }
}
