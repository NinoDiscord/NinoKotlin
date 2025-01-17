package dev.augu.nino.commands.moderation

import dev.augu.nino.butterfly.command.CommandContext
import dev.augu.nino.common.entities.Action
import dev.augu.nino.common.entities.ModerationCommand
import dev.augu.nino.services.cases.ICaseService
import dev.augu.nino.services.discord.IDiscordService
import dev.augu.nino.services.moderation.IModerationService
import dev.augu.nino.services.moderation.log.IModerationLogService
import dev.augu.nino.services.scheduler.ISchedulerService
import java.time.Instant
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.User

class UnbanCommand(
    private val discordService: IDiscordService,
    private val moderationService: IModerationService,
    private val caseService: ICaseService,
    private val moderationLogService: IModerationLogService,
    private val schedulerService: ISchedulerService
) : ModerationCommand(
        "unban",
        "Unbans the user",
        userPermissions = Permission.BAN_MEMBERS.rawValue,
        botPermissions = Permission.BAN_MEMBERS.rawValue) {

    override suspend fun execute(ctx: CommandContext) {
        val arguments = extractArguments(ctx.args)

        if (arguments == null) {
            ctx.replyTranslate("unbanCommandInvalidArguments", mapOf("prefix" to ctx.prefix))
            return
        }

        val userToUnban = discordService.extractUserFromId(arguments.userId)

        if (userToUnban == null) {
            ctx.replyTranslate("unableToFindUser", mapOf("userId" to arguments.userId))
            return
        }

        if (discordService.getUserBan(arguments.userId, ctx.guild!!) == null) {
            ctx.replyTranslate("unbanCommandNotBanned")
            return
        }

        moderationService.unban(arguments.userId, ctx.guild!!, arguments.reason)

        if (arguments.reason.isNullOrEmpty()) {
            ctx.replyTranslate("unbanCommandSuccess", mapOf("user" to userToUnban.name, "prefix" to ctx.prefix))
        } else {
            ctx.replyTranslate("unbanCommandSuccessReason", mapOf("user" to userToUnban.name, "reason" to arguments.reason, "prefix" to ctx.prefix))
        }

        schedulerService.stopScheduledJob(Action.UNBAN, userToUnban.id, ctx.guild!!.id)

        resolveRelatedBanCase(ctx, userToUnban)

        val case = caseService.createUnbanCase(userToUnban.id, ctx.author.id, null, Instant.now(), null, ctx.guild!!.id, false, arguments.reason, null)
        moderationLogService.log(case)
    }

    private suspend fun resolveRelatedBanCase(ctx: CommandContext, userToUnban: User) {
        val caseToResolve = caseService.findLastCaseByActionAndUser(ctx.guild!!.id, Action.BAN, userToUnban.id)
        if (caseToResolve != null) {
            caseService.resolveCase(caseToResolve)
            moderationLogService.updateLog(caseToResolve)
        }
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
