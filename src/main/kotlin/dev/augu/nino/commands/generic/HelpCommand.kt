package dev.augu.nino.commands.generic

import dev.augu.nino.butterfly.ButterflyClient
import dev.augu.nino.butterfly.command.Command
import dev.augu.nino.butterfly.command.CommandContext
import dev.augu.nino.common.entities.GenericCommand
import dev.augu.nino.common.util.createEmbed
import net.dv8tion.jda.api.Permission

class HelpCommand(private val client: ButterflyClient): GenericCommand(
        "help",
        "Gives a list of ${client.jda.selfUser.asTag}'s commands or shows documentation on a specific command or module",
        "halp", "h", "cmds", "commands", "?"
) {
    override suspend fun execute(ctx: CommandContext) {
        val prefix = client.guildSettingsLoader.load(ctx.guild!!)?.prefix ?: "x!"

        if (ctx.args.isEmpty()) {
            return sendCommandList(prefix, ctx)
        }

        val commandName = ctx.args[0]
        val command = client.commands[commandName] ?: client.aliases[commandName]
        val category = client.commands.values.filter { category == commandName }

        if (command != null) {
            return sendCommandDocs(command, prefix, ctx)
        }
        if (category.isNotEmpty()) {
            return sendCategoryDocs(category, prefix, commandName, ctx)
        }

        ctx.reply("Command or module `$commandName` was not found.")
    }

    private suspend fun sendCategoryDocs(category: List<Command>, prefix: String, commandName: String, ctx: CommandContext) {
        val commands = category.joinToString("\n") { "• `$prefix${it.name}`: **${it.description}**" }

        val embed = createEmbed {
            setTitle("[ Module ${commandName.capitalize()} ]")
            setDescription(commands)
        }

        ctx.reply(embed.build())
    }

    private suspend fun sendCommandDocs(command: Command, prefix: String, ctx: CommandContext) {
        val aliases = if (command.aliases.isNotEmpty()) command.aliases.joinToString(", ") { "`$it`" } else "None"
        val botPermissions = if (command.botPermissions != 0L) Permission.getPermissions(command.botPermissions).joinToString(", ") { "`${it.getName()}`" } else "None"
        val userPermissions = if (command.userPermissions != 0L) Permission.getPermissions(command.botPermissions).joinToString(", ") { "`${it.getName()}`" } else "None"

        val embed = createEmbed {
            setTitle("[ Command $prefix${command.name} ]")
            setDescription("> :pencil2: **${command.description}**")
            addField("• Category", command.category.capitalize(), true)
            addField("• Aliases", aliases, true)
            addField("• Guild Only", if (command.guildOnly) "Yes" else "No", true)
            addField("• Owner Only", if (command.ownerOnly) "Yes" else "No", true)
            addField("• Bot Permissions", botPermissions, true)
            addField("• User Permissions", userPermissions, true)
        }

        ctx.reply(embed.build())
    }

    private suspend fun sendCommandList(prefix: String, ctx: CommandContext) {
        val commands = client.commands.filter { !it.value.ownerOnly }
        val categories = HashMap<String, MutableList<String>>()

        for (cmd in commands.values) {
            if (cmd.category == "system") continue

            categories.putIfAbsent(cmd.category, mutableListOf())
            categories[cmd.category]!!.add(cmd.name)
        }

        val embed = createEmbed {
            setTitle("${client.jda.selfUser.asTag} | Commands List")
            setDescription("More documentation on a command is available on the [website](https://nino.augu.dev)!")
            setFooter("To get extra documentation, run \"${prefix}help [command]\" | ${commands.size} Commands Available")
        }

        for ((category, cmds) in categories) {
            val all = cmds.joinToString(", ") { "`$it`" }
            embed.addField("• ${category.capitalize()} [${categories[category]!!.size}]", all, false)
        }

        ctx.reply(embed.build())
    }
}