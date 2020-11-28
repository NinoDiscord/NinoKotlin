package dev.augu.nino.services.moderation.log

import club.minnced.jda.reactor.asMono
import dev.augu.nino.common.entities.cases.Case
import dev.augu.nino.services.cases.ICaseService
import dev.augu.nino.services.settings.IGuildSettingsService
import kotlinx.coroutines.reactive.awaitSingle
import kotlinx.coroutines.reactive.awaitSingleOrNull

class ModerationLogService(private val guildSettingsService: IGuildSettingsService, private val caseService: ICaseService) :
    IModerationLogService {
    override suspend fun log(case: Case) {
        val channel = guildSettingsService.getModLogChannel(case.guildId) ?: return

        val msg = channel.sendMessage(case.toEmbed()).asMono().awaitSingle()
        case.modLogMessageId = msg.id
        caseService.updateCase(case)
    }

    override suspend fun updateLog(case: Case) {
        val channel = guildSettingsService.getModLogChannel(case.guildId) ?: return

        if (case.modLogMessageId != null) {
            val msg = channel.editMessageById(case.modLogMessageId!!, case.toEmbed()).asMono().awaitSingleOrNull()
            if (msg == null) {
                case.modLogMessageId = null
                caseService.updateCase(case)
            }
            return
        }
    }
}
