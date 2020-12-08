package dev.augu.nino.commands.moderation

import dev.augu.nino.butterfly.command.CommandContext
import dev.augu.nino.common.entities.Action
import dev.augu.nino.common.entities.ModerationCommand
import dev.augu.nino.common.entities.cases.BanCase
import dev.augu.nino.common.entities.cases.MuteCase
import dev.augu.nino.common.util.formatDurationLong
import dev.augu.nino.common.util.formatDurationShort
import dev.augu.nino.common.util.parseDuration
import dev.augu.nino.services.cases.ICaseService
import dev.augu.nino.services.moderation.log.IModerationLogService
import dev.augu.nino.services.scheduler.ISchedulerService
import dev.augu.nino.services.scheduler.job.UnbanSchedulerJob
import dev.augu.nino.services.scheduler.job.UnmuteSchedulerJob
import java.time.Duration
import java.time.Instant
import net.dv8tion.jda.api.Permission

class UpdateTimeCommand(private val caseService: ICaseService, private val logService: IModerationLogService, private val schedulerService: ISchedulerService): ModerationCommand(
    "updatetime",
    "Updates the time of a case with a temporary action",

    userPermissions = Permission.getRaw(Permission.KICK_MEMBERS),
    botPermissions = Permission.getRaw(Permission.MESSAGE_MANAGE)
) {

    override suspend fun execute(ctx: CommandContext) {
        val args = extractArguments(ctx.args)
        if (args == null) {
            ctx.replyTranslate("updatetimeCommandNoArgs", mapOf("prefix" to ctx.prefix))
            return
        }

        if (args.caseID == null) {
            ctx.replyTranslate("updatetimeCommandInvalidCase", mapOf("caseID" to ctx.args[0]))
            return
        }

        if (args.duration == null || (args.duration < Duration.ofSeconds(15) || args.duration > Duration.ofDays(7))) {
            ctx.replyTranslate("updatetimeCommandInvalidDuration", mapOf("caseID" to ctx.args[0]))
            return
        }

        val case = caseService.getCase(args.caseID, ctx.guild!!.id)
        if (case == null) {
            ctx.replyTranslate("updatetimeCommandNoCaseFound", mapOf("caseID" to ctx.args[0]))
            return
        }
        if (case !is BanCase && case !is MuteCase) {
            ctx.replyTranslate("updatetimeCommandCaseNotTemp", mapOf("caseID" to ctx.args[0]))
            return
        }

        if (case.resolved) {
            ctx.replyTranslate("updatetimeCommandCaseAlreadyResolved", mapOf("caseID" to ctx.args[0]))
            return
        }

        when (case) {
            is BanCase -> {
                case.time = args.duration.toMillis()
                schedulerService.stopScheduledJob(Action.UNBAN, case.targetUserId, case.guildId)
                schedulerService.scheduleJob(UnbanSchedulerJob(startTime = case.createdAt, duration = case.time!!, targetUserId = case.targetUserId, guildId = case.guildId, reason = "Time's up!"))
            }
            is MuteCase -> {
                case.time = args.duration.toMillis()
                schedulerService.stopScheduledJob(Action.UNMUTE, case.targetUserId, case.guildId)
                schedulerService.scheduleJob(UnmuteSchedulerJob(startTime = case.createdAt, duration = case.time!!, targetUserId = case.targetUserId, guildId = case.guildId, reason = "Time's up!"))
            }
            else -> {}
        }
        case.lastChangedAt = Instant.now()
        case.lastModeratorId = ctx.author.id
        caseService.updateCase(case)
        logService.updateLog(case)

        val remainingDuration = args.duration
            .minusMillis(Instant.now().toEpochMilli())
            .plusMillis(case.createdAt.toEpochMilli())
            .coerceAtLeast(Duration.ZERO)

        ctx.replyTranslate("updatetimeCommandSuccess", mapOf(
            "remainingDuration" to formatDurationShort(remainingDuration),
            "duration" to formatDurationLong(args.duration),
            "caseID" to args.caseID.toString())
        )
    }

    data class Arguments(val caseID: Int?, val duration: Duration?)

    private fun extractArguments(args: Array<String>): Arguments? {
        if (args.isEmpty()) return null

        val caseID = try {
            Integer.parseInt(args[0])
        } catch (ex: Exception) {
            null
        }

        val duration = parseDuration(args.drop(1).joinToString(" ").trim())

        return Arguments(caseID, duration)
    }
}
