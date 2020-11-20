package dev.augu.nino.services.postgres

import java.sql.Connection
import org.jetbrains.exposed.sql.Database

interface IPostgresService {

    fun createConnection(): Connection

    val database: Database
}
