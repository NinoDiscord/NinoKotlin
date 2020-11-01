package dev.augu.nino.commands.generic

import dev.augu.nino.butterfly.command.CommandContext
import dev.augu.nino.common.entities.GenericCommand
import dev.augu.nino.butterfly.ButterflyClient
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
                embed.addField("• $category [${categories[category]!!.size}]", all, false)
            }

            ctx.reply(embed.build())
        } else {
            val search = ctx.args[0]
            val command = client.commands[search] ?: client.aliases[search]

            if (command != null) {
                val aliases = if (command.aliases.isNotEmpty()) command.aliases.joinToString(", ") { "`$it`" } else "None"
                val botPermissions = if (command.botPermissions != 0L) Permission.getPermissions(command.botPermissions).joinToString(", ") { "`${it.getName()}`" } else "None"
                val userPermissions = if (command.userPermissions != 0L) Permission.getPermissions(command.botPermissions).joinToString(", ") { "`${it.getName()}`" } else "None"

                val embed = createEmbed {
                    setTitle("[ Command $prefix${command.name} ]")
                    setDescription("> :pencil2: **${command.description}**")
                    addField("• Category", command.category, true)
                    addField("• Aliases", aliases, true)
                    addField("• Guild Only", if (command.guildOnly) "Yes" else "No", true)
                    addField("• Owner Only", if (command.ownerOnly) "Yes" else "No", true)
                    addField("• Bot Permissions", botPermissions, true)
                    addField("• User Permissions", userPermissions, true)
                }

                ctx.reply(embed.build())
            } else {
                val modules = client.commands.filter { it.value.category == search }
                if (modules.isNotEmpty()) {
                    val commands = modules.values.joinToString("\n") { "• `$prefix${it.name}`: **${it.description}**"}

                    val embed = createEmbed {
                        setTitle("[ Module $search ]")
                        setDescription(commands)
                    }

                    ctx.reply(embed.build())
                } else {
                    ctx.reply("Command or module `$search` was not found?")
                }
            }
        }
    }
}