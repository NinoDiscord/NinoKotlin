package dev.augu.nino.services.discord

import club.minnced.jda.reactor.asMono
import kotlinx.coroutines.reactive.awaitSingle
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.User
import net.dv8tion.jda.api.exceptions.ErrorResponseException
import net.dv8tion.jda.api.requests.ErrorResponse

private val SNOWFLAKE_REGEX = Regex("(?:<@!?)?(\\d+)(?:>)?")

class DiscordService : IDiscordService {

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

    override suspend fun extractUserFromId(userId: String, jda: JDA): User? {
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
}