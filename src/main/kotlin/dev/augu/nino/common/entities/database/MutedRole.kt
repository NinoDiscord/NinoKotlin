package dev.augu.nino.common.entities.database

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column

object MutedRoles : IntIdTable() {
    val guildId: Column<String> = varchar("guild_id", 18).uniqueIndex()
    val mutedRoleId: Column<String> = varchar("muted_role_id", 18).uniqueIndex()
}

class MutedRole(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<MutedRole>(MutedRoles)

    var guildId by MutedRoles.guildId
    var mutedRoleId by MutedRoles.mutedRoleId
}
