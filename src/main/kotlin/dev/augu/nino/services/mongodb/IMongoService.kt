package dev.augu.nino.services.mongodb

import com.mongodb.reactivestreams.client.ClientSession
import org.litote.kmongo.coroutine.CoroutineDatabase

interface IMongoService {
    val database: CoroutineDatabase

    suspend fun createSession(): ClientSession
}