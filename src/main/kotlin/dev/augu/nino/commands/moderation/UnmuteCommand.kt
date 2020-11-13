package dev.augu.nino.commands.moderation

import dev.augu.nino.butterfly.command.CommandContext
import dev.augu.nino.common.entities.ModerationCommand
import dev.augu.nino.services.discord.IDiscordService
import dev.augu.nino.services.moderation.IModerationService
import dev.augu.nino.services.settings.IGuildSettingsService
import net.dv8tion.jda.api.Permission

class UnmuteCommand(private val discordService: IDiscordService, private val moderationService: IModerationService, private val guildSettingsService: IGuildSettingsService) : ModerationCommand(
        "unmute",
        "Unmutes the user",
        userPermissions = Permission.BAN_MEMBERS.rawValue,
        botPermissions = Permission.BAN_MEMBERS.rawValue) {

    override suspend fun execute(ctx: CommandContext) {
        val arguments = extractArguments(ctx.args)

        if (arguments == null) {
            ctx.replyTranslate("unmuteCommandInvalidArguments", mapOf("prefix" to ctx.prefix))
            return
        }

        val memberToUnmute = discordService.extractMemberFromId(arguments.userId, ctx.guild!!)

        if (memberToUnmute == null) {
            ctx.replyTranslate("unableToFindMember", mapOf("memberId" to arguments.userId))
            return
        }

        if (!memberToUnmute.roles.contains(guildSettingsService.getMutedRole(ctx.guild!!.id))) {
            ctx.replyTranslate("unmuteCommandNotMuted")
            return
        }

        moderationService.unmute(memberToUnmute, arguments.reason)

        if (arguments.reason.isNullOrEmpty()) {
            ctx.replyTranslate("unmuteCommandSuccess", mapOf("user" to memberToUnmute.user.name, "prefix" to ctx.prefix))
        } else {
            ctx.replyTranslate("unmuteCommandSuccessReason", mapOf("user" to memberToUnmute.user.name, "reason" to arguments.reason, "prefix" to ctx.prefix))
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