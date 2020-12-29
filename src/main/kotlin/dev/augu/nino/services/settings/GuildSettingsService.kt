package dev.augu.nino.services.settings

import dev.augu.nino.common.entities.database.CaseId
import dev.augu.nino.common.entities.database.CaseIds
import dev.augu.nino.common.entities.database.GuildGeneralSettings
import dev.augu.nino.common.entities.database.GuildGeneralSettingsTable
import dev.augu.nino.common.entities.database.ModLogSettings
import dev.augu.nino.common.entities.database.ModLogSettingsTable
import dev.augu.nino.common.entities.database.MutedRole
import dev.augu.nino.common.entities.database.MutedRoles
import dev.augu.nino.services.discord.IDiscordService
import dev.augu.nino.services.locale.ILocaleService
import dev.augu.nino.services.postgres.IPostgresService
import net.dv8tion.jda.api.entities.Role
import net.dv8tion.jda.api.entities.TextChannel
import org.jetbrains.exposed.sql.transactions.transaction

class GuildSettingsService(
    private val discordService: IDiscordService,
    private val postgresService: IPostgresService,
    private val localeService: ILocaleService
) : IGuildSettingsService {

    override fun getGuildGeneralSettings(guildId: String): GuildGeneralSettings {
        return transaction(postgresService.database) {
            GuildGeneralSettings
                    .find { GuildGeneralSettingsTable.guildId eq guildId }
                    .firstOrNull()
                    ?: GuildGeneralSettings.new {
                        this.guildId = guildId
                        this.localeCode = localeService.defaultLocale.code
                    }
            }
    }

    override fun getModLogChannel(guildId: String): TextChannel? =
        transaction(postgresService.database) {
            val channelID = ModLogSettings
                    .find { ModLogSettingsTable.guildID eq guildId }
                    .firstOrNull()?.modLogChannel ?: return@transaction null

            discordService.extractTextChannelFromId(channelID)
        }

    override fun setModLogChannel(channelId: String, guildId: String): Unit =
        transaction(postgresService.database) {
            ModLogSettings.new {
                this.modLogChannel = channelId
                this.guildID = guildId
            }
        }

    override fun getMutedRole(guildId: String): Role? {
        return transaction(postgresService.database) {
            val roleId = MutedRole.find { MutedRoles.guildId eq guildId }.firstOrNull()?.mutedRoleId
                    ?: return@transaction null
            discordService.extractRoleFromId(roleId)
        }
    }

    override fun setMutedRole(roleId: String, guildId: String) {
        return transaction(postgresService.database) {
            MutedRole.new {
                this.guildId = guildId
                this.mutedRoleId = roleId
            }
        }
    }

    override fun incrementAndGetLastCaseId(guildId: String): Int {
        return transaction(postgresService.database) {
            val caseId = CaseId.find { CaseIds.guildId eq guildId }.firstOrNull()

            if (caseId != null) {
                caseId.lastCaseId++
                caseId.flush()
                caseId.lastCaseId
            } else {
                CaseId.new {
                    this.guildId = guildId
                    this.lastCaseId = 1
                }.lastCaseId
            }
        }
    }
}
