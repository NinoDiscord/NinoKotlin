package dev.augu.nino.services.redis

import io.lettuce.core.api.StatefulRedisConnection
import io.lettuce.core.codec.RedisCodec

interface IRedisService {
    fun createConnection(): StatefulRedisConnection<String, String>
    fun <K, V> createConnection(codec: RedisCodec<K, V>): StatefulRedisConnection<K, V>
}
