package dev.augu.nino.services.discord

import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.Role
import net.dv8tion.jda.api.entities.User

interface IDiscordService {
    fun extractSnowflake(text: String): String?

    suspend fun extractMemberFromId(memberId: String, guild: Guild): Member?

    suspend fun extractUserFromId(userId: String): User?

    suspend fun getUserBan(userId: String, guild: Guild): Guild.Ban?

    suspend fun createMutedRole(guild: Guild): Role
}
