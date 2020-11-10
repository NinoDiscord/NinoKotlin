package dev.augu.nino.services.postgres

import dev.augu.nino.configuration.Configuration
import io.r2dbc.postgresql.PostgresqlConnectionConfiguration
import io.r2dbc.postgresql.PostgresqlConnectionFactory
import io.r2dbc.postgresql.api.PostgresqlConnection
import kotlinx.coroutines.reactive.awaitSingle
import reactor.core.publisher.Mono

class PostgresService(config: Configuration) {
    private val postgresqlConnectionFactory: PostgresqlConnectionFactory

    init {
        val postgresConfig = config.postgres
        postgresqlConnectionFactory = PostgresqlConnectionFactory(PostgresqlConnectionConfiguration.builder()
                .host(postgresConfig.host)
                .port(postgresConfig.port)
                .database(postgresConfig.database)
                .username(postgresConfig.username)
                .password(postgresConfig.password)
                .schema(postgresConfig.schema)
                .build())
    }

    fun createConnectionMono(): Mono<PostgresqlConnection> = Mono.from(postgresqlConnectionFactory.create())

    suspend fun createConnection(): PostgresqlConnection = postgresqlConnectionFactory.create().awaitSingle()
}