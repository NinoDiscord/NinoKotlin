package dev.augu.nino.services.moderation

import club.minnced.jda.reactor.asMono
import dev.augu.nino.services.discord.IDiscordService
import dev.augu.nino.services.scheduler.ISchedulerService
import dev.augu.nino.services.scheduler.job.UnbanSchedulerJob
import dev.augu.nino.services.scheduler.job.UnmuteSchedulerJob
import dev.augu.nino.services.settings.IGuildSettingsService
import kotlinx.coroutines.reactive.awaitSingleOrNull
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.Member
import java.time.Duration

class ModerationService(private val discordService: IDiscordService, private val guildSettingsService: IGuildSettingsService, private val schedulerService: ISchedulerService) : IModerationService {
    override suspend fun ban(member: Member, reason: String?, delDays: Int?) {
        member.ban(delDays ?: 7, reason).asMono().awaitSingleOrNull()
    }

    override suspend fun ban(memberId: String, guild: Guild, reason: String?, delDays: Int?) {
        guild.ban(memberId, delDays ?: 7, reason).asMono().awaitSingleOrNull()
    }

    override suspend fun tempban(member: Member, duration: Duration, reason: String?, delDays: Int?) {
        tempban(member.id, member.guild, duration, reason, delDays)
    }

    override suspend fun tempban(userId: String, guild: Guild, duration: Duration, reason: String?, delDays: Int?) {
        ban(userId, guild, reason, delDays)

        schedulerService.scheduleJob(UnbanSchedulerJob(duration = duration.toMillis(), targetUserId = userId, guildId = guild.id, reason = "Time's up!"))
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
        ban(userId, guild, reason, delDays)
        unban(userId, guild, reason)
    }

    override suspend fun mute(member: Member, reason: String?) {
        mute(member.id, member.guild, reason)
    }

    override suspend fun mute(memberId: String, guild: Guild, reason: String?) {
        var mutedRole = guildSettingsService.getMutedRole(guild.id)

        if (mutedRole == null) {
            mutedRole = discordService.createMutedRole(guild)
            guildSettingsService.setMutedRole(mutedRole.id, guild.id)
        }

        guild.addRoleToMember(memberId, mutedRole)
                .reason(reason)
                .asMono()
                .awaitSingleOrNull()
    }

    override suspend fun tempmute(member: Member, duration: Duration, reason: String?) {
        tempmute(member.id, member.guild, duration, reason)
    }

    override suspend fun tempmute(memberId: String, guild: Guild, duration: Duration, reason: String?) {
        mute(memberId, guild, reason)

        schedulerService.scheduleJob(UnmuteSchedulerJob(duration = duration.toMillis(), targetUserId = memberId, guildId = guild.id, reason = "Time's up!"))
    }

    override suspend fun unmute(member: Member, reason: String?) {
        unmute(member.id, member.guild, reason)
    }

    override suspend fun unmute(memberId: String, guild: Guild, reason: String?) {
        val mutedRole = guildSettingsService.getMutedRole(guild.id) ?: return

        guild.removeRoleFromMember(memberId, mutedRole)
                .reason(reason)
                .asMono()
                .awaitSingleOrNull()
    }
}
