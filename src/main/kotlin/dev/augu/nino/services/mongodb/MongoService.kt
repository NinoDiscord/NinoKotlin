package dev.augu.nino.services.mongodb

import com.mongodb.ConnectionString
import dev.augu.nino.configuration.Configuration
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

class MongoService(config: Configuration) : IMongoService {
    private val mongoClient = KMongo.createClient(ConnectionString(config.mongodb.connectionString)).coroutine
    private val databaseName = config.mongodb.database

    override suspend fun createSession() = mongoClient.startSession()

    override val database: CoroutineDatabase
        get() = mongoClient.getDatabase(databaseName)
}