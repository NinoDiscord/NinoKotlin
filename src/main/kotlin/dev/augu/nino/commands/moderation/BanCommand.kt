package dev.augu.nino.commands.moderation

import dev.augu.nino.butterfly.command.CommandContext
import dev.augu.nino.common.entities.ModerationCommand
import dev.augu.nino.common.util.formatDurationLong
import dev.augu.nino.common.util.parseDuration
import dev.augu.nino.services.discord.IDiscordService
import dev.augu.nino.services.moderation.IModerationService
import net.dv8tion.jda.api.Permission
import java.time.Duration

class BanCommand(
    private val moderationService: IModerationService,
    private val discordService: IDiscordService
)
    : ModerationCommand(
        "ban",
        "Bans the user",
        userPermissions = Permission.BAN_MEMBERS.rawValue,
        botPermissions = Permission.BAN_MEMBERS.rawValue) {

    override suspend fun execute(ctx: CommandContext) {
        val arguments = extractArguments(ctx.args)

        if (arguments == null) {
            ctx.replyTranslate("banCommandInvalidArguments", mapOf("prefix" to ctx.prefix))
            return
        }

        if (arguments.duration != null && (arguments.duration <= Duration.ofSeconds(15) || arguments.duration >= Duration.ofDays(7))) {
            ctx.replyTranslate("banCommandInvalidDuration")
            return
        }

        val userToBan = discordService.extractUserFromId(arguments.userId, ctx.message.jda)

        if (userToBan == null) {
            ctx.replyTranslate("unableToFindUser", mapOf("userId" to arguments.userId))
            return
        }

        if (discordService.getUserBan(arguments.userId, ctx.guild!!) != null) {
            ctx.replyTranslate("banCommandAlreadyBanned")
            return
        }

        if (arguments.duration == null) {
            moderationService.ban(arguments.userId, ctx.guild!!, arguments.reason, null)

            if (arguments.reason.isNullOrEmpty()) {
                ctx.replyTranslate("banCommandSuccess", mapOf("user" to userToBan.name))
            } else {
                ctx.replyTranslate("banCommandSuccessReason", mapOf("user" to userToBan.name, "reason" to arguments.reason))
            }
        } else {
            moderationService.tempban(userToBan.id, ctx.guild!!, arguments.duration, arguments.reason, null)

            if (arguments.reason.isNullOrEmpty()) {
                ctx.replyTranslate("banCommandSuccessTime", mapOf("user" to userToBan.name, "duration" to formatDurationLong(arguments.duration)))
            } else {
                ctx.replyTranslate("banCommandSuccessReasonTime", mapOf("user" to userToBan.name, "duration" to formatDurationLong(arguments.duration), "reason" to arguments.reason))
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
