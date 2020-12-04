package dev.augu.nino.commands.moderation

import dev.augu.nino.butterfly.command.CommandContext
import dev.augu.nino.common.entities.ModerationCommand
import dev.augu.nino.services.cases.ICaseService
import dev.augu.nino.services.moderation.log.IModerationLogService
import net.dv8tion.jda.api.Permission
import java.time.Instant

class ReasonCommand(private val caseService: ICaseService, private val logService: IModerationLogService): ModerationCommand(
    "reason",
    "Updates the reason of a specific case",

    userPermissions = Permission.getRaw(Permission.KICK_MEMBERS),
    botPermissions = Permission.getRaw(Permission.MESSAGE_MANAGE)
) {

    override suspend fun execute(ctx: CommandContext) {
        val args = extractArguments(ctx.args)
        if (args == null) {
            ctx.replyTranslate("reasonCommandNoArgs")
            return
        }

        if (args.caseID == null) {
            ctx.replyTranslate("reasonCommandInvalidCase", mapOf("caseID" to ctx.args[0]))
            return
        }

        val case = caseService.getCase(args.caseID, ctx.guild!!.id)
        if (case == null) {
            ctx.replyTranslate("reasonCommandNoCaseFound", mapOf("caseID" to args.caseID.toString()))
            return
        }

        case.reason = args.reason
        case.lastChangedAt = Instant.now()
        case.lastModeratorId = ctx.author.id
        caseService.updateCase(case)
        logService.updateLog(case)

        ctx.replyTranslate("reasonCommandUpdated", mapOf(
            "reason" to args.reason,
            "caseID" to args.caseID.toString())
        )
    }

    data class Arguments(val caseID: Int?, val reason: String)

    private fun extractArguments(args: Array<String>): Arguments? {
        if (args.isEmpty()) return null

        val caseID = try {
            Integer.parseInt(args[0])
        } catch (ex: Exception) {
            null
        }

        val reason = args.drop(1).joinToString(" ").trim()

        return Arguments(caseID, reason)
    }
}
