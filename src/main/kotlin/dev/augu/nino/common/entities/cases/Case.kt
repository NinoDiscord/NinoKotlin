package dev.augu.nino.common.entities.cases

import dev.augu.nino.common.entities.Action
import dev.augu.nino.common.util.createEmbed
import dev.augu.nino.common.util.formatDateLong
import dev.augu.nino.common.util.formatDurationLong
import dev.augu.nino.services.discord.IDiscordService
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.entities.User
import org.koin.core.context.GlobalContext
import org.litote.kmongo.Id
import org.litote.kmongo.newId
import java.awt.Color
import java.time.Duration
import java.time.Instant
import java.time.ZoneOffset

@Serializable
sealed class Case {
    abstract val action: Action

    abstract val targetUserId: String

    /**
     * The moderator that created the case
     */
    abstract val initialModeratorId: String
    /**
     * The moderator that last touched the case
     */
    abstract var lastModeratorId: String?

    @Contextual abstract val createdAt: Instant

    @Contextual abstract var lastChangedAt: Instant?

    abstract val guildId: String

    abstract var resolved: Boolean

    abstract val caseId: Int

    abstract var reason: String?

    abstract var modLogMessageId: String?

    @Contextual @SerialName("_id") abstract val mongoId: Id<Case>

    fun isChanged(): Boolean {
        return lastChangedAt != null
    }

    abstract fun isLogged(): Boolean

    abstract suspend fun toEmbed(): MessageEmbed
}

@Serializable
data class BanCase(
    override val targetUserId: String,
    override val initialModeratorId: String,
    override var lastModeratorId: String?,
    @Contextual override val createdAt: Instant,
    @Contextual override var lastChangedAt: Instant?,
    override val guildId: String,
    override var resolved: Boolean,
    override val caseId: Int,
    override var reason: String?,
    override var modLogMessageId: String?,
    var time: Long?,
    val soft: Boolean,
    @Contextual @SerialName("_id") override val mongoId: Id<Case> = newId()
) : Case() {
    override val action = Action.BAN

    override fun isLogged(): Boolean = true

    override suspend fun toEmbed(): MessageEmbed {
        val koin = GlobalContext.get()
        return createCaseEmbedBuilder(this, koin.get()).apply {
            if (time != null) {
                addField("Duration", formatDurationLong(Duration.ofMillis(time!!)), false)
            }
        }.build()
    }
}

@Serializable
data class UnbanCase(
    override val targetUserId: String,
    override val initialModeratorId: String,
    override var lastModeratorId: String?,
    @Contextual override val createdAt: Instant,
    @Contextual override var lastChangedAt: Instant?,
    override val guildId: String,
    override var resolved: Boolean,
    override val caseId: Int,
    override var reason: String?,
    override var modLogMessageId: String?,
    @Contextual @SerialName("_id") override val mongoId: Id<Case> = newId()
) : Case() {
    override val action = Action.UNBAN

    override fun isLogged(): Boolean = true

    override suspend fun toEmbed(): MessageEmbed {
        val koin = GlobalContext.get()
        return createCaseEmbedBuilder(this, koin.get()).build()
    }
}

@Serializable
data class MuteCase(
    override val targetUserId: String,
    override val initialModeratorId: String,
    override var lastModeratorId: String?,
    @Contextual override val createdAt: Instant,
    @Contextual override var lastChangedAt: Instant?,
    override val guildId: String,
    override var resolved: Boolean,
    override val caseId: Int,
    override var reason: String?,
    override var modLogMessageId: String?,
    var time: Long?,
    @Contextual @SerialName("_id") override val mongoId: Id<Case> = newId()
) : Case() {
    override val action = Action.MUTE

    override fun isLogged(): Boolean = true

    override suspend fun toEmbed(): MessageEmbed {
        val koin = GlobalContext.get()
        return createCaseEmbedBuilder(this, koin.get()).apply {
            if (time != null) {
                addField("Duration", formatDurationLong(Duration.ofMillis(time!!)), false)
            }
        }.build()
    }
}

@Serializable
data class UnmuteCase(
    override val targetUserId: String,
    override val initialModeratorId: String,
    override var lastModeratorId: String?,
    @Contextual override val createdAt: Instant,
    @Contextual override var lastChangedAt: Instant?,
    override val guildId: String,
    override var resolved: Boolean,
    override val caseId: Int,
    override var reason: String?,
    override var modLogMessageId: String?,
    @Contextual @SerialName("_id") override val mongoId: Id<Case> = newId()
) : Case() {
    override val action = Action.UNMUTE

    override fun isLogged(): Boolean = true

    override suspend fun toEmbed(): MessageEmbed {
        val koin = GlobalContext.get()
        return createCaseEmbedBuilder(this, koin.get()).build()
    }
}

@Serializable
data class KickCase(
    override val targetUserId: String,
    override val initialModeratorId: String,
    override var lastModeratorId: String?,
    @Contextual override val createdAt: Instant,
    @Contextual override var lastChangedAt: Instant?,
    override val guildId: String,
    override var resolved: Boolean,
    override val caseId: Int,
    override var reason: String?,
    override var modLogMessageId: String?,
    @Contextual @SerialName("_id") override val mongoId: Id<Case> = newId()
) : Case() {
    override val action = Action.KICK

    override fun isLogged(): Boolean = true

    override suspend fun toEmbed(): MessageEmbed {
        val koin = GlobalContext.get()
        return createCaseEmbedBuilder(this, koin.get()).build()
    }
}

private suspend fun createCaseEmbedBuilder(case: Case, discordService: IDiscordService): EmbedBuilder {
    val targetUser = discordService.extractUserFromId(case.targetUserId)
    val initialModerator = discordService.extractUserFromId(case.initialModeratorId)
    val lastModerator = if (case.lastModeratorId != null) discordService.extractUserFromId(case.lastModeratorId!!) else null

    return createEmbed {
        setTitle(getCaseTitle(case))
        setColor(getCaseColor(case))
        addField("User", getUserDisplay(targetUser, case.targetUserId), false)
        if (case.lastModeratorId != null && case.initialModeratorId != case.lastModeratorId) {
            addField("Initial Moderator", getUserDisplay(initialModerator, case.initialModeratorId), false)
            addField("Current Moderator", getUserDisplay(lastModerator, case.lastModeratorId!!), false)
        } else {
            addField("Moderator", getUserDisplay(initialModerator, case.initialModeratorId), false)
        }
        if (!case.reason.isNullOrBlank()) {
            addField("Reason", case.reason!!, false)
        }
        setFooter(getCaseFooter(case))
    }
}

private fun getCaseTitle(case: Case): String {
    return if (case.resolved) {
        "Case #${case.caseId} - ${getCaseTitleAction(case)} (Resolved)"
    } else {
        "Case #${case.caseId} - ${getCaseTitleAction(case)}"
    }
}

private fun getCaseTitleAction(case: Case): String = when (case) {
    is BanCase -> if (case.soft) "Soft Ban" else "Ban"
    is UnbanCase -> "Unban"
    is MuteCase -> "Mute"
    is UnmuteCase -> "Unmute"
    is KickCase -> "Kick"
}

private fun getCaseColor(case: Case): Color = when (case) {
    is BanCase -> if (case.soft) Color.BLUE else Color.RED
    is UnbanCase, is UnmuteCase -> Color.GREEN
    is MuteCase, is KickCase -> Color.YELLOW
}

private fun getUserDisplay(user: User?, userId: String): String =
    "${user?.name ?: "Deleted User"}#${user?.discriminator ?: "0000"} (ID: $userId)"

private fun getCaseFooter(case: Case): String {
    return if (case.isChanged()) {
        "Created: ${formatDateLong(case.createdAt.atOffset(ZoneOffset.UTC))} | Last Modified: ${formatDateLong(case.lastChangedAt!!.atOffset(ZoneOffset.UTC))}"
    } else {
        "Created: ${formatDateLong(case.createdAt.atOffset(ZoneOffset.UTC))}"
    }
}
