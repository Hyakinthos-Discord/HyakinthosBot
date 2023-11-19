package me.thecuddlybear.Bot

import com.kotlindiscord.kord.extensions.ExtensibleBot
import dev.kord.common.entity.PresenceStatus
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

        presence {
            status = PresenceStatus.DoNotDisturb

            watching("Ruiz hacking")
        }

        applicationCommands {
            defaultGuild("851831355229601844")
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
