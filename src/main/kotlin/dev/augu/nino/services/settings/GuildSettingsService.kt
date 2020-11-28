package dev.augu.nino.services.settings

import dev.augu.nino.common.entities.database.*
import dev.augu.nino.services.locale.ILocaleService
import dev.augu.nino.services.postgres.IPostgresService
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.Role
import net.dv8tion.jda.api.entities.TextChannel
import org.jetbrains.exposed.sql.transactions.transaction

class GuildSettingsService(
    private val jda: JDA,
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

    override fun getMutedRole(guildId: String): Role? {
        return transaction(postgresService.database) {
            val roleId = MutedRole.find { MutedRoles.guildId eq guildId }.firstOrNull()?.mutedRoleId
                    ?: return@transaction null
            jda.getRoleById(roleId)
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
}
