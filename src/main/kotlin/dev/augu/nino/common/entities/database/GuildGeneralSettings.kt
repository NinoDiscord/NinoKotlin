package dev.augu.nino.common.entities.database

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column

object GuildGeneralSettingsTable : IntIdTable() {
    val guildId: Column<String> = varchar("guild_id", 18).uniqueIndex()
    val prefix: Column<String?> = varchar("prefix", 20).nullable()
    val localeCode: Column<String> = varchar("locale_code", 8)
}

class GuildGeneralSettings(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<GuildGeneralSettings>(GuildGeneralSettingsTable)

    var guildId by GuildGeneralSettingsTable.guildId
    var prefix by GuildGeneralSettingsTable.prefix
    var localeCode by GuildGeneralSettingsTable.localeCode
}