package dev.augu.nino.services.moderation.log

import dev.augu.nino.common.entities.cases.Case

interface IModerationLogService {
    suspend fun log(case: Case)

    suspend fun updateLog(case: Case)
}
