package dev.augu.nino.services.mongodb

import com.mongodb.ConnectionString
import dev.augu.nino.configuration.Configuration
import dev.augu.nino.services.scheduler.job.*
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo
import org.litote.kmongo.serialization.registerModule

class MongoService(config: Configuration) : IMongoService {
    private val mongoClient = KMongo.createClient(ConnectionString(config.mongodb.connectionString)).coroutine
    private val databaseName = config.mongodb.database

    init {
        registerModule(SerializersModule {
            polymorphic(SchedulerJob::class) {
                subclass(UnmuteSchedulerJob::class, UnmuteSchedulerJob.serializer())
                subclass(UnbanSchedulerJob::class, UnbanSchedulerJob.serializer())
                subclass(MuteSchedulerJob::class, MuteSchedulerJob.serializer())
                subclass(KickSchedulerJob::class, KickSchedulerJob.serializer())
                subclass(BanSchedulerJob::class, BanSchedulerJob.serializer())
            }
        })
    }

    override suspend fun createSession() = mongoClient.startSession()

    override val database: CoroutineDatabase
        get() = mongoClient.getDatabase(databaseName)
}
