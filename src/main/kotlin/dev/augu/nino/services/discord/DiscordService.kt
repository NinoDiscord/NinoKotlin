package dev.augu.nino.services.discord

import club.minnced.jda.reactor.asMono
import kotlinx.coroutines.reactive.awaitSingle
import kotlinx.coroutines.reactive.awaitSingleOrNull
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.Role
import net.dv8tion.jda.api.entities.User
import net.dv8tion.jda.api.exceptions.ErrorResponseException
import net.dv8tion.jda.api.requests.ErrorResponse

private val SNOWFLAKE_REGEX = Regex("(?:<@!?)?(\\d+)(?:>)?")

class DiscordService(private val jda: JDA) : IDiscordService {

    override fun extractSnowflake(text: String): String? {
        return SNOWFLAKE_REGEX.find(text)?.groupValues?.get(1)
    }

    override suspend fun extractMemberFromId(memberId: String, guild: Guild): Member? {
        return try {
            guild.getMemberById(memberId) ?: guild.retrieveMemberById(memberId).asMono().awaitSingle()
        } catch (e: ErrorResponseException) {
            null
        }
    }

    override suspend fun extractUserFromId(userId: String): User? {
        return try {
            jda.getUserById(userId) ?: jda.retrieveUserById(userId).asMono().awaitSingle()
        } catch (e: ErrorResponseException) {
            null
        }
    }

    override suspend fun getUserBan(userId: String, guild: Guild): Guild.Ban? {
        return try {
            guild.retrieveBanById(userId).asMono().awaitSingle()
        } catch (e: ErrorResponseException) {
            when (e.errorResponse) {
                ErrorResponse.UNKNOWN_BAN -> null
                else -> throw e // rethrow permission issues
            }
        }
    }

    override suspend fun createMutedRole(guild: Guild): Role {
        val role = guild.createRole()
                .setName("Muted")
                .setHoisted(false)
                .setMentionable(false)
                .setPermissions(0)
                .reason("Creating Nino muted role")
                .asMono()
                .awaitSingle()

        for (channel in guild.textChannels) {
            if (guild.selfMember.hasPermission(channel, Permission.VIEW_CHANNEL, Permission.MANAGE_CHANNEL)) {
                channel.manager.putPermissionOverride(role, mutableListOf(), mutableListOf(Permission.MESSAGE_WRITE)).asMono().awaitSingleOrNull()
            }
        }

        return role
    }
}
