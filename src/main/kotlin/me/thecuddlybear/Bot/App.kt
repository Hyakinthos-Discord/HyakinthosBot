package me.thecuddlybear.Bot

import com.kotlindiscord.kord.extensions.ExtensibleBot
import dev.kord.gateway.Intent
import dev.kord.gateway.PrivilegedIntent
import me.thecuddlybear.Bot.extensions.SlapExtension

private val TOKEN = "MTEyMTAyMjM0ODkzNDUyNTAxOA.GC6BFi.nZ6RAobisQLDglXL92RGQHwoSkNiTSl6-DyxOQ"

suspend fun main() {

    val bot = ExtensibleBot(TOKEN){
        extensions {
            add(::SlapExtension)
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
