package dev.augu.nino.services.scheduler

import dev.augu.nino.common.entities.Action
import dev.augu.nino.services.scheduler.job.SchedulerJob

interface ISchedulerService {
    suspend fun scheduleJob(schedulerJob: SchedulerJob)

    suspend fun stopScheduledJob(action: Action, targetUserId: String, guildId: String)

    suspend fun stopScheduledJob(schedulerJobId: String)

    suspend fun loadAndScheduleAllJobs()
}