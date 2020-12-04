package dev.augu.nino.commands.moderation

import dev.augu.nino.butterfly.command.CommandContext
import dev.augu.nino.common.entities.ModerationCommand
import dev.augu.nino.services.cases.ICaseService
import net.dv8tion.jda.api.Permission

class ReasonCommand(private val cases: ICaseService): ModerationCommand(
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

        val case = cases.getCase(args.caseID, ctx.guild!!.id)
        if (case == null) {
            ctx.replyTranslate("reasonCommandNoCaseFound", mapOf("caseID" to args.caseID.toString()))
            return
        }

        // fuck how do i update it in the db?
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
            return null
        }

        val reason = args.drop(1).joinToString(" ").trim()

        return Arguments(caseID, reason)
    }
}
