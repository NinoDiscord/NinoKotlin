package dev.augu.nino.services.postgres

import org.jetbrains.exposed.sql.Database
import java.sql.Connection

interface IPostgresService {

    fun createConnection(): Connection

    val database: Database
}
