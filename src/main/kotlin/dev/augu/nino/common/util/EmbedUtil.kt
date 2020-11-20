package dev.augu.nino.common.util

import net.dv8tion.jda.api.EmbedBuilder
import java.awt.Color

/**
 * Creates a structural embed with `block` being the embed content
 * @return A [net.dv8tion.jda.api.EmbedBuilder] embed to send to Discord, must call <code>.build</code>
 * to make it a sendable embed!
 */
fun createEmbed(block: EmbedBuilder.() -> Unit) =
        EmbedBuilder()
                .setColor(Color.decode("#FFB2BA"))
                .apply(block)
