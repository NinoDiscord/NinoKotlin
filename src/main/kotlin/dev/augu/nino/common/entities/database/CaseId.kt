package dev.augu.nino.common.entities.database

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column

object CaseIds : IntIdTable() {
    val guildId: Column<String> = varchar("guild_id", 18).uniqueIndex()
    val lastCaseId: Column<Int> = integer("last_case_id")
}

class CaseId(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<CaseId>(CaseIds)

    var guildId by CaseIds.guildId
    var lastCaseId by CaseIds.lastCaseId
}
