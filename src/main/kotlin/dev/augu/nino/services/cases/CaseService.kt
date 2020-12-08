package dev.augu.nino.services.cases

import dev.augu.nino.common.entities.Action
import dev.augu.nino.common.entities.cases.BanCase
import dev.augu.nino.common.entities.cases.Case
import dev.augu.nino.common.entities.cases.KickCase
import dev.augu.nino.common.entities.cases.MuteCase
import dev.augu.nino.common.entities.cases.UnbanCase
import dev.augu.nino.common.entities.cases.UnmuteCase
import dev.augu.nino.services.mongodb.IMongoService
import dev.augu.nino.services.settings.IGuildSettingsService
import java.time.Instant
import org.litote.kmongo.coroutine.aggregate
import org.litote.kmongo.descending
import org.litote.kmongo.eq
import org.litote.kmongo.match
import org.litote.kmongo.sort

class CaseService(private val mongoService: IMongoService, private val guildSettingsService: IGuildSettingsService) :
    ICaseService {
    private val collection
        get() = mongoService.database.getCollection<Case>()

    override suspend fun getCase(caseId: Int, guildId: String): Case? {
        return collection.find(Case::caseId eq caseId, Case::guildId eq guildId).first()
    }

    override suspend fun updateCase(case: Case) {
        collection.save(case)
    }

    override suspend fun resolveCase(case: Case) {
        case.resolved = true
        case.lastChangedAt = Instant.now()
        collection.save(case)
    }

    override suspend fun findLastCaseByActionAndUser(guildId: String, action: Action, userId: String): Case? {
        return collection
            .aggregate<Case>(
                match(
                    Case::guildId eq guildId,
                    Case::targetUserId eq userId,
                    Case::action eq action
                ),
                sort(
                    descending(Case::caseId)
                )
            ).first()
    }

    override suspend fun createBanCase(targetUserId: String, initialModeratorId: String, lastModeratorId: String?, createdAt: Instant, lastChangedAt: Instant?, guildId: String, resolved: Boolean, reason: String?, modLogMessageId: String?, time: Long?, soft: Boolean): BanCase {
        val case = BanCase(targetUserId, initialModeratorId, lastModeratorId, createdAt, lastChangedAt, guildId, resolved, guildSettingsService.incrementAndGetLastCaseId(guildId), reason, modLogMessageId, time, soft)
        collection.save(case)
        return case
    }

    override suspend fun createUnbanCase(targetUserId: String, initialModeratorId: String, lastModeratorId: String?, createdAt: Instant, lastChangedAt: Instant?, guildId: String, resolved: Boolean, reason: String?, modLogMessageId: String?): UnbanCase {
        val case = UnbanCase(targetUserId, initialModeratorId, lastModeratorId, createdAt, lastChangedAt, guildId, resolved, guildSettingsService.incrementAndGetLastCaseId(guildId), reason, modLogMessageId)
        collection.save(case)
        return case
    }

    override suspend fun createMuteCase(targetUserId: String, initialModeratorId: String, lastModeratorId: String?, createdAt: Instant, lastChangedAt: Instant?, guildId: String, resolved: Boolean, reason: String?, modLogMessageId: String?, time: Long?): MuteCase {
        val case = MuteCase(targetUserId, initialModeratorId, lastModeratorId, createdAt, lastChangedAt, guildId, resolved, guildSettingsService.incrementAndGetLastCaseId(guildId), reason, modLogMessageId, time)
        collection.save(case)
        return case
    }

    override suspend fun createUnmuteCase(targetUserId: String, initialModeratorId: String, lastModeratorId: String?, createdAt: Instant, lastChangedAt: Instant?, guildId: String, resolved: Boolean, reason: String?, modLogMessageId: String?): UnmuteCase {
        val case = UnmuteCase(targetUserId, initialModeratorId, lastModeratorId, createdAt, lastChangedAt, guildId, resolved, guildSettingsService.incrementAndGetLastCaseId(guildId), reason, modLogMessageId)
        collection.save(case)
        return case
    }

    override suspend fun createKickCase(targetUserId: String, initialModeratorId: String, lastModeratorId: String?, createdAt: Instant, lastChangedAt: Instant?, guildId: String, resolved: Boolean, reason: String?, modLogMessageId: String?): KickCase {
        val case = KickCase(targetUserId, initialModeratorId, lastModeratorId, createdAt, lastChangedAt, guildId, resolved, guildSettingsService.incrementAndGetLastCaseId(guildId), reason, modLogMessageId)
        collection.save(case)
        return case
    }
}
