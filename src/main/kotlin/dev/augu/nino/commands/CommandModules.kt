package dev.augu.nino.commands

import dev.augu.nino.commands.generic.genericCommandModule
import dev.augu.nino.commands.moderation.moderationModule
import dev.augu.nino.commands.settings.settingsModule

val commandModules = genericCommandModule + moderationModule + settingsModule
