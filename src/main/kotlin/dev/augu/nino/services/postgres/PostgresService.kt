package dev.augu.nino.services.postgres

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import dev.augu.nino.configuration.Configuration
import java.sql.Connection
import org.jetbrains.exposed.sql.Database

class PostgresService(config: Configuration) : IPostgresService {
    private val dataSource: HikariDataSource

    init {
        val postgresConfig = config.postgres
        dataSource = HikariDataSource(HikariConfig().let {
            it.jdbcUrl = postgresConfig.jdbcUrl
            it.username = postgresConfig.username
            it.password = postgresConfig.password
            it.schema = postgresConfig.schema ?: "public"
            it.driverClassName = "org.postgresql.Driver"
            it
        })
    }

    override fun createConnection(): Connection = dataSource.connection

    override val database: Database
        get() = Database.connect(dataSource)
}
