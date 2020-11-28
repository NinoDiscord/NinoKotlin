package dev.augu.nino.services.cases

import dev.augu.nino.common.entities.Action
import dev.augu.nino.common.entities.cases.BanCase
import dev.augu.nino.common.entities.cases.Case
import dev.augu.nino.common.entities.cases.KickCase
import dev.augu.nino.common.entities.cases.MuteCase
import dev.augu.nino.common.entities.cases.UnbanCase
import dev.augu.nino.common.entities.cases.UnmuteCase
import java.time.Instant

interface ICaseService {
    suspend fun getCase(caseId: Int, guildId: String): Case?

    suspend fun updateCase(case: Case)

    suspend fun resolveCase(case: Case)

    suspend fun findLastCaseByActionAndUser(guildId: String, action: Action, userId: String): Case?

    suspend fun createBanCase(
        targetUserId: String,
        initialModeratorId: String,
        lastModeratorId: String?,
        createdAt: Instant,
        lastChangedAt: Instant?,
        guildId: String,
        resolved: Boolean,
        reason: String?,
        modLogMessageId: String?,
        time: Long?,
        soft: Boolean
    ): BanCase

    suspend fun createUnbanCase(
        targetUserId: String,
        initialModeratorId: String,
        lastModeratorId: String?,
        createdAt: Instant,
        lastChangedAt: Instant?,
        guildId: String,
        resolved: Boolean,
        reason: String?,
        modLogMessageId: String?
    ): UnbanCase

    suspend fun createMuteCase(
        targetUserId: String,
        initialModeratorId: String,
        lastModeratorId: String?,
        createdAt: Instant,
        lastChangedAt: Instant?,
        guildId: String,
        resolved: Boolean,
        reason: String?,
        modLogMessageId: String?,
        time: Long?
    ): MuteCase

    suspend fun createUnmuteCase(
        targetUserId: String,
        initialModeratorId: String,
        lastModeratorId: String?,
        createdAt: Instant,
        lastChangedAt: Instant?,
        guildId: String,
        resolved: Boolean,
        reason: String?,
        modLogMessageId: String?
    ): UnmuteCase

    suspend fun createKickCase(
        targetUserId: String,
        initialModeratorId: String,
        lastModeratorId: String?,
        createdAt: Instant,
        lastChangedAt: Instant?,
        guildId: String,
        resolved: Boolean,
        reason: String?,
        modLogMessageId: String?
    ): KickCase
}
