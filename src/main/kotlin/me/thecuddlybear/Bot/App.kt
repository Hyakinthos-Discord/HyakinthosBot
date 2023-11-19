package me.thecuddlybear.Bot

import com.kotlindiscord.kord.extensions.ExtensibleBot
import dev.kord.gateway.Intent
import dev.kord.gateway.PrivilegedIntent
import me.thecuddlybear.Bot.extensions.HypixelExtension
import me.thecuddlybear.Bot.extensions.MusicExtension
import me.thecuddlybear.Bot.extensions.SlapExtension
import org.dotenv.vault.dotenvVault

public val dotenv = dotenvVault()
private val TOKEN = dotenv["TOKEN"]

suspend fun main() {

    val bot = ExtensibleBot(TOKEN){
        extensions {
            add(::SlapExtension)
            add(::MusicExtension)
            add(::HypixelExtension)
        }

        applicationCommands {
            defaultGuild("688559550051385372")
        }

        chatCommands  {
            defaultPrefix = "pp!"
            enabled = true
            invokeOnMention = true
        }

        @OptIn(PrivilegedIntent::class)
        intents {
            +Intent.GuildMembers
            +Intent.MessageContent
            +Intent.GuildMessages
        }
    }

    bot.start()

}
