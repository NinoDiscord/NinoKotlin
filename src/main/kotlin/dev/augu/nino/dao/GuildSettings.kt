package dev.augu.nino.dao

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column

object GuildSettingsTable : IdTable<String>() {
    override val id: Column<EntityID<String>> = varchar("id", 18).entityId()
    val prefix = varchar("prefix", 5).nullable()
    val mutedRole = varchar("muted_role", 18).nullable()
    val modLogChannel = varchar("mod_log_channel", 18).nullable()
    val locale = varchar("locale", 10).nullable()

    override val primaryKey: PrimaryKey? = PrimaryKey(id, name = "PK_GuildSettings_Id")

}

class GuildSettings(id: EntityID<String>) : Entity<String>(id) {
    companion object : EntityClass<String, GuildSettings>(GuildSettingsTable)

    val prefix by GuildSettingsTable.prefix
    val mutedRole by GuildSettingsTable.mutedRole
    val modLogChannel by GuildSettingsTable.modLogChannel
    val locale by GuildSettingsTable.locale
}