package dev.augu.nino.services.moderation

import club.minnced.jda.reactor.asMono
import kotlinx.coroutines.reactive.awaitSingleOrNull
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.Member
import java.time.Duration

class ModerationService : IModerationService {
    override suspend fun ban(member: Member, reason: String?, delDays: Int?) {
        member.ban(delDays ?: 7, reason).asMono().awaitSingleOrNull()
    }

    override suspend fun ban(memberId: String, guild: Guild, reason: String?, delDays: Int?) {
        guild.ban(memberId, delDays ?: 7, reason).asMono().awaitSingleOrNull()
    }

    override suspend fun tempban(member: Member, duration: Duration, reason: String?, delDays: Int?) {
        TODO("Not yet implemented")
    }

    override suspend fun tempban(userId: String, guild: Guild, duration: Duration, reason: String?, delDays: Int?) {
        //TODO("Not yet implemented")
    }

    override suspend fun kick(member: Member, reason: String?) {
        member.kick(reason).asMono().awaitSingleOrNull()
    }

    override suspend fun kick(memberId: String, guild: Guild, reason: String?) {
        guild.kick(memberId, reason).asMono().awaitSingleOrNull()
    }

    override suspend fun unban(userId: String, guild: Guild, reason: String?) {
        guild.unban(userId).reason(reason).asMono().awaitSingleOrNull()
    }

    override suspend fun softban(member: Member, reason: String?, delDays: Int?) {
        ban(member, reason, delDays)
        unban(member.id, member.guild, reason)
    }

    override suspend fun softban(userId: String, guild: Guild, reason: String?, delDays: Int?) {
        TODO("Not yet implemented")
    }

    override suspend fun mute(member: Member, reason: String?) {
        TODO("Not yet implemented")
    }

    override suspend fun mute(memberId: String, guild: Guild, reason: String?) {
        TODO("Not yet implemented")
    }

    override suspend fun tempmute(member: Member, duration: Duration, reason: String?) {
        TODO("Not yet implemented")
    }

    override suspend fun tempmute(memberId: String, guild: Guild, duration: Duration, reason: String?) {
        TODO("Not yet implemented")
    }

    override suspend fun unmute(member: Member, reason: String?) {
        TODO("Not yet implemented")
    }

    override suspend fun unmute(memberId: String, guild: Guild, reason: String?) {
        TODO("Not yet implemented")
    }
}