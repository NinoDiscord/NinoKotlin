package dev.augu.nino.services.redis

import dev.augu.nino.configuration.Configuration
import io.lettuce.core.RedisClient
import io.lettuce.core.RedisURI
import io.lettuce.core.api.StatefulRedisConnection
import io.lettuce.core.codec.RedisCodec

class RedisService(config: Configuration) : IRedisService {

    private val redisURI: RedisURI

    init {
        val redisConfig = config.redis
        if (redisConfig.url != null) {
            redisURI = RedisURI.create(redisConfig.url)
        } else {
            redisURI = RedisURI.create(redisConfig.host!!, redisConfig.port ?: 6543).let {
                if (redisConfig.password != null && redisConfig.password.isNotEmpty()) {
                    it.password = redisConfig.password.toCharArray()
                }
                if (redisConfig.database != null) {
                    it.database = redisConfig.database
                }
                it
            }
        }
    }

    private val redisClient: RedisClient = RedisClient.create(redisURI)

    override fun createConnection(): StatefulRedisConnection<String, String> = redisClient.connect()

    override fun <K, V> createConnection(codec: RedisCodec<K, V>): StatefulRedisConnection<K, V> = redisClient.connect(codec)
}
