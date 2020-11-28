package dev.augu.nino.common.entities.database

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column

object ModLogSettingsTable: IntIdTable() {
    val modLogChannel: Column<String> = varchar("mod_log_channel_id", 18)
    val guildID: Column<String> = varchar("guild_id", 18).uniqueIndex()
}

class ModLogSettings(id: EntityID<Int>): IntEntity(id) {
    companion object: IntEntityClass<ModLogSettings>(ModLogSettingsTable)

    var modLogChannel by ModLogSettingsTable.modLogChannel
    var guildID by ModLogSettingsTable.guildID
}
