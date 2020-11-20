package dev.augu.nino.services.moderation

import java.time.Duration
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.Member

interface IModerationService {
    suspend fun ban(member: Member, reason: String?, delDays: Int?)

    suspend fun ban(memberId: String, guild: Guild, reason: String?, delDays: Int?)

    suspend fun tempban(member: Member, duration: Duration, reason: String?, delDays: Int?)

    suspend fun tempban(userId: String, guild: Guild, duration: Duration, reason: String?, delDays: Int?)

    suspend fun kick(member: Member, reason: String?)

    suspend fun kick(memberId: String, guild: Guild, reason: String?)

    suspend fun unban(userId: String, guild: Guild, reason: String?)

    suspend fun softban(member: Member, reason: String?, delDays: Int?)

    suspend fun softban(userId: String, guild: Guild, reason: String?, delDays: Int?)

    suspend fun mute(member: Member, reason: String?)

    suspend fun mute(memberId: String, guild: Guild, reason: String?)

    suspend fun tempmute(member: Member, duration: Duration, reason: String?)

    suspend fun tempmute(memberId: String, guild: Guild, duration: Duration, reason: String?)

    suspend fun unmute(member: Member, reason: String?)

    suspend fun unmute(memberId: String, guild: Guild, reason: String?)
}
