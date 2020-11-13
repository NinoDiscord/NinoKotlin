package dev.augu.nino.commands.moderation

import dev.augu.nino.butterfly.command.CommandContext
import dev.augu.nino.common.entities.ModerationCommand
import dev.augu.nino.services.discord.IDiscordService
import dev.augu.nino.services.moderation.IModerationService
import net.dv8tion.jda.api.Permission

class UnbanCommand(val discordService: IDiscordService, val moderationService: IModerationService) : ModerationCommand(
        "unban",
        "Unbans the user",
        userPermissions = Permission.BAN_MEMBERS.rawValue,
        botPermissions = Permission.BAN_MEMBERS.rawValue) {

    override suspend fun execute(ctx: CommandContext) {
        if (ctx.args.isEmpty()) {
            ctx.replyTranslate("unbanCommandInvalidArguments")
            return
        }

        val arguments = extractArguments(ctx.args)

        if (arguments.userId == null) {
            ctx.replyTranslate("unbanCommandInvalidArguments")
            return
        }

        val userToUnban = discordService.extractUserFromId(arguments.userId, ctx.message.jda)

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
            ctx.replyTranslate("unbanCommandSuccess", mapOf("user" to userToUnban.name))
        } else {
            ctx.replyTranslate("unbanCommandSuccessReason", mapOf("user" to userToUnban.name, "reason" to arguments.reason))
        }

        // TODO("Add logging")
    }

    private data class Arguments(val userId: String?, val reason: String?)

    private fun extractArguments(args: Array<String>): Arguments {
        val userToBanId = discordService.extractSnowflake(args[0])
        val reason = args.drop(1).joinToString(" ").trim()

        return Arguments(userToBanId, reason)
    }
}