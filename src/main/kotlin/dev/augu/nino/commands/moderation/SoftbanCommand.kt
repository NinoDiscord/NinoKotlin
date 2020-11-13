package dev.augu.nino.commands.moderation

import dev.augu.nino.butterfly.command.CommandContext
import dev.augu.nino.common.entities.ModerationCommand
import dev.augu.nino.services.discord.IDiscordService
import dev.augu.nino.services.moderation.IModerationService
import net.dv8tion.jda.api.Permission

class SoftbanCommand(
        private val moderationService: IModerationService,
        private val discordService: IDiscordService)
    : ModerationCommand(
        "softban",
        "Bans the user and immediately unbans the user",
        userPermissions = Permission.BAN_MEMBERS.rawValue,
        botPermissions = Permission.BAN_MEMBERS.rawValue) {
    override suspend fun execute(ctx: CommandContext) {
        val arguments = extractArguments(ctx.args)

        if (arguments == null) {
            ctx.replyTranslate("softbanCommandInvalidArguments", mapOf("prefix" to ctx.prefix))
            return
        }

        val userToSoftban = discordService.extractUserFromId(arguments.userId, ctx.message.jda)

        if (userToSoftban == null) {
            ctx.replyTranslate("unableToFindUser", mapOf("userId" to arguments.userId))
            return
        }

        if (discordService.getUserBan(arguments.userId, ctx.guild!!) != null) {
            ctx.replyTranslate("softbanCommandAlreadyBanned")
            return
        }

        moderationService.softban(arguments.userId, ctx.guild!!, arguments.reason, null)

        if (arguments.reason.isNullOrEmpty()) {
            ctx.replyTranslate("softbanCommandSuccess", mapOf("user" to userToSoftban.name, "prefix" to ctx.prefix))
        } else {
            ctx.replyTranslate("softbanCommandSuccessReason", mapOf("user" to userToSoftban.name, "reason" to arguments.reason, "prefix" to ctx.prefix))
        }

        // TODO("Add logging")
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