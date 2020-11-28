package dev.augu.nino.common.entities.database

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column

object ModLogSettingsTable: IntIdTable() {
    val guildLogChannel: Column<String> = varchar("guild_log_channel", 18)
    val modLogChannel: Column<String> = varchar("mod_log_channel", 18)
    val guildID: Column<String> = varchar("guild_id", 18).uniqueIndex()
}

class ModLogSettings(id: EntityID<Int>): IntEntity(id) {
    companion object: IntEntityClass<ModLogSettings>(ModLogSettingsTable)

    var guildLogChannel by ModLogSettingsTable.guildLogChannel
    var modLogChannel by ModLogSettingsTable.modLogChannel
    var guildID by ModLogSettingsTable.guildID
}
