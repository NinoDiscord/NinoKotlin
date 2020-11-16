package dev.augu.nino.services.scheduler.persistance

import dev.augu.nino.common.entities.Action
import dev.augu.nino.services.mongodb.IMongoService
import dev.augu.nino.services.scheduler.job.SchedulerJob
import org.litote.kmongo.coroutine.CoroutineCollection
import org.litote.kmongo.eq

class MongoPersistor(private val mongoService: IMongoService) {
    private val collection: CoroutineCollection<SchedulerJob>
        get() = mongoService.database.getCollection()

    suspend fun persistSchedulerJob(schedulerJob: SchedulerJob) {
        collection.save(schedulerJob)
    }

    suspend fun getSchedulerJob(action: Action, targetUserId: String, guildId: String): SchedulerJob? {
        return collection.findOne(SchedulerJob::action eq action, SchedulerJob::targetUserId eq targetUserId, SchedulerJob::guildId eq guildId)
    }

    suspend fun completeSchedulerJob(schedulerJobId: String) {
        collection.deleteOneById(schedulerJobId)
    }

    suspend fun completeSchedulerJob(schedulerJob: SchedulerJob) {
        collection.deleteOneById(schedulerJob._id)
    }

    suspend fun getAllScheduledJobs(): List<SchedulerJob> {
        return collection.find().toList()
    }
}