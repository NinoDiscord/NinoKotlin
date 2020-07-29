package dev.augu.nino.dao

import dev.augu.nino.dao.utils.array
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.CharColumnType
import org.jetbrains.exposed.sql.Column

object LogSettingsTable : IdTable<String>() {
    override val id: Column<EntityID<String>> = varchar("guild_id", 18).entityId()
    val channelId = varchar("channel_id", 18).nullable()
    val ignoredChannels = array<String>("ignored_channels", CharColumnType(18))

}

class LogSettings(id: EntityID<String>) : Entity<String>(id) {

}