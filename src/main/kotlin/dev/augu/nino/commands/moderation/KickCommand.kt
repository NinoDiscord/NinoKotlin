package dev.augu.nino.commands.moderation

import dev.augu.nino.butterfly.command.CommandContext
import dev.augu.nino.common.entities.ModerationCommand
import dev.augu.nino.services.cases.ICaseService
import dev.augu.nino.services.discord.IDiscordService
import dev.augu.nino.services.moderation.IModerationService
import dev.augu.nino.services.moderation.log.IModerationLogService
import java.time.Instant
import net.dv8tion.jda.api.Permission

class KickCommand(
    private val moderationService: IModerationService,
    private val discordService: IDiscordService,
    private val caseService: ICaseService,
    private val moderationLogService: IModerationLogService
)
    : ModerationCommand(
        "kick",
        "Kicks the user",
        userPermissions = Permission.KICK_MEMBERS.rawValue,
        botPermissions = Permission.KICK_MEMBERS.rawValue) {
    override suspend fun execute(ctx: CommandContext) {
        val arguments = extractArguments(ctx.args)

        if (arguments == null) {
            ctx.replyTranslate("kickCommandInvalidArguments", mapOf("prefix" to ctx.prefix))
            return
        }

        val memberToKick = discordService.extractMemberFromId(arguments.userId, ctx.guild!!)

        if (memberToKick == null) {
            ctx.replyTranslate("unableToFindMember", mapOf("memberId" to arguments.userId))
            return
        }

        moderationService.kick(memberToKick, arguments.reason)

        if (arguments.reason.isNullOrEmpty()) {
            ctx.replyTranslate("kickCommandSuccess", mapOf("user" to memberToKick.user.name))
        } else {
            ctx.replyTranslate("kickCommandSuccessReason", mapOf("user" to memberToKick.user.name, "reason" to arguments.reason))
        }

        val case = caseService.createKickCase(memberToKick.id, ctx.author.id, null, Instant.now(), null, ctx.guild!!.id, false, arguments.reason, null)
        moderationLogService.log(case)
    }

    private data class Arguments(val userId: String, val reason: String?)

    private fun extractArguments(args: Array<String>): Arguments? {
        if (args.isEmpty()) {
            return null
        }

        val userId = discordService.extractSnowflake(args[0]) ?: return null
        val reason = args.drop(1).joinToString(" ").trim()

        return Arguments(userId, reason)
    }
}
