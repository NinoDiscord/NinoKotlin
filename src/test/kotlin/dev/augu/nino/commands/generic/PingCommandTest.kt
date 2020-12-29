package dev.augu.nino.commands.generic

import dev.augu.nino.butterfly.command.CommandContext
import dev.augu.nino.butterfly.i18n.I18nLanguage
import dev.augu.nino.butterfly.util.edit
import dev.augu.nino.services.discord.IDiscordService
import dev.augu.nino.testutils.testModule
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.koin.KoinListener
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.spyk
import java.time.Instant
import java.time.ZoneOffset
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import net.dv8tion.jda.api.entities.Message
import org.koin.core.component.inject
import org.koin.test.KoinTest

@ExperimentalCoroutinesApi
class PingCommandTest : DescribeSpec(), KoinTest {

    init {
        listener(KoinListener(testModule))
    }

    init {
        val discordService by inject<IDiscordService>()
        describe("Integration Tests - Ping Command") {
            val cmd = spyk(PingCommand(discordService))
            val language = I18nLanguage("", mapOf(
                    "pingCommandOldMessage" to "Calculating...",
                    "pingCommandNewMessage" to "Shard \${id} | Ping: \${messageLatency}ms | Websocket: \${shard}ms"
            ))

            it("should print the latency") {
                val ctx = mockk<CommandContext>()
                val sampleMsg = mockk<Message>()
                val nextMsg = mockk<Message>()
                val instant = Instant.now()

                every { ctx.message.timeCreated } returns instant.atOffset(ZoneOffset.UTC)
                coEvery { ctx.language() } returns language
                every { discordService.gatewayPing } returns 55
                coEvery { ctx.reply(any<CharSequence>()) } returns sampleMsg
                coEvery { ctx.replyTranslate(any()) } coAnswers {
                    ctx.reply(language.translate(it.invocation.args[0] as String))
                }
                every { sampleMsg.timeCreated } returns instant.plusMillis(500).atOffset(ZoneOffset.UTC)
                mockkStatic("dev.augu.nino.butterfly.util.MessageExtensions")
                coEvery { sampleMsg.edit(any<CharSequence>()) } returns nextMsg

                runBlocking {
                    cmd.execute(ctx)
                }

                coVerify {
                    ctx.reply("Calculating...")
                }
                coVerify {
                    sampleMsg.edit("Shard 0 | Ping: 500ms | Websocket: 55ms")
                }
            }
        }
    }
}
