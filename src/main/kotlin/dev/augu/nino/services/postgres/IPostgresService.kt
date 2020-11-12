package dev.augu.nino.services.postgres

import io.r2dbc.postgresql.api.PostgresqlConnection
import reactor.core.publisher.Mono

interface IPostgresService {
    fun createConnectionMono(): Mono<PostgresqlConnection>

    suspend fun createConnection(): PostgresqlConnection
}