package dev.augu.nino.services.discord

import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.Role
import net.dv8tion.jda.api.entities.SelfUser
import net.dv8tion.jda.api.entities.TextChannel
import net.dv8tion.jda.api.entities.User

/**
 * Mockable interface that wraps JDA, makes it easy to unit-test behavior.
 */
interface IDiscordService {
    val selfUser: SelfUser

    val guildCount: Long

    val userCount: Long

    val channelCount: Long

    val gatewayPing: Long

    fun getShardInfo(shardId: Int): JDA.ShardInfo

    fun extractSnowflake(text: String): String?

    fun extractGuildFromId(guildId: String): Guild?

    fun extractTextChannelFromId(textChannelId: String): TextChannel?

    fun extractRoleFromId(roleId: String): Role?

    suspend fun extractMemberFromId(memberId: String, guild: Guild): Member?

    suspend fun extractUserFromId(userId: String): User?

    suspend fun getUserBan(userId: String, guild: Guild): Guild.Ban?

    suspend fun createMutedRole(guild: Guild): Role
}
