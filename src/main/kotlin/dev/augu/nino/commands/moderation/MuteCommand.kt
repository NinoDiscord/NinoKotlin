package dev.augu.nino.commands.moderation

import dev.augu.nino.butterfly.command.CommandContext
import dev.augu.nino.common.entities.ModerationCommand
import dev.augu.nino.common.entities.NinoGuildSettings
import dev.augu.nino.common.util.formatDurationLong
import dev.augu.nino.common.util.parseDuration
import dev.augu.nino.services.discord.IDiscordService
import dev.augu.nino.services.moderation.IModerationService
import net.dv8tion.jda.api.Permission
import java.time.Duration

class MuteCommand(
    private val discordService: IDiscordService,
    private val moderationService: IModerationService
) : ModerationCommand(
        "mute",
        "Mutes the user",
        "silence",
        "stfu",
        userPermissions = Permission.KICK_MEMBERS.rawValue,
        botPermissions = Permission.MANAGE_ROLES.rawValue + Permission.MANAGE_CHANNEL.rawValue
) {

    override suspend fun execute(ctx: CommandContext) {
        val arguments = extractArguments(ctx.args)

        if (arguments == null) {
            ctx.replyTranslate("muteCommandInvalidArguments", mapOf("prefix" to ctx.prefix))
            return
        }

        if (arguments.duration != null && (arguments.duration < Duration.ofSeconds(15) || arguments.duration > Duration.ofDays(7))) {
            ctx.replyTranslate("muteCommandInvalidDuration")
            return
        }

        val memberToMute = discordService.extractMemberFromId(arguments.userId, ctx.guild!!)

        if (memberToMute == null) {
            ctx.replyTranslate("unableToFindMember", mapOf("memberId" to arguments.userId))
            return
        }

        val settings = ctx.settings<NinoGuildSettings>()

        if (memberToMute.roles.contains(settings?.mutedRole)) {
            ctx.replyTranslate("muteCommandAlreadyMuted")
            return
        }

        if (arguments.duration == null) {
            moderationService.mute(memberToMute, arguments.reason)

            if (arguments.reason.isNullOrEmpty()) {
                ctx.replyTranslate("muteCommandSuccess", mapOf("user" to memberToMute.user.name))
            } else {
                ctx.replyTranslate("muteCommandSuccessReason", mapOf("user" to memberToMute.user.name, "reason" to arguments.reason))
            }
        } else {
            moderationService.tempmute(memberToMute, arguments.duration, arguments.reason)

            if (arguments.reason.isNullOrEmpty()) {
                ctx.replyTranslate("muteCommandSuccessTime", mapOf("user" to memberToMute.user.name, "duration" to formatDurationLong(arguments.duration)))
            } else {
                ctx.replyTranslate("muteCommandSuccessReasonTime", mapOf("user" to memberToMute.user.name, "duration" to formatDurationLong(arguments.duration), "reason" to arguments.reason))
            }
        }
        // TODO("Add logging")
    }

    private data class Arguments(val userId: String, val reason: String?, val duration: Duration?)

    private fun extractArguments(args: Array<String>): Arguments? {
        if (args.isEmpty()) {
            return null
        }

        val userId = discordService.extractSnowflake(args[0]) ?: return null
        val reason = args.drop(1).joinToString(" ").takeWhile { char -> char != '|' }.trim()
        val duration = parseDuration(args.drop(1).joinToString(" ").dropWhile { char -> char != '|' }.trim())

        return Arguments(userId, reason, duration)
    }
}
