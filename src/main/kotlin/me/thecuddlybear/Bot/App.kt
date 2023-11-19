package me.thecuddlybear.Bot

import com.kotlindiscord.kord.extensions.ExtensibleBot
import dev.arbjerg.lavalink.protocol.v4.LoadResult
import dev.arbjerg.lavalink.protocol.v4.Track
import dev.kord.common.entity.PresenceStatus
import dev.kord.common.entity.Snowflake
import dev.kord.core.entity.Guild
import dev.kord.gateway.Intent
import dev.kord.gateway.PrivilegedIntent
import dev.schlaubi.lavakord.LavaKord
import dev.schlaubi.lavakord.kord.lavakord
import dev.schlaubi.lavakord.plugins.lavasrc.LavaSrc
import dev.schlaubi.lavakord.plugins.sponsorblock.Sponsorblock
import me.thecuddlybear.Bot.extensions.AnimeActionsExtension
import me.thecuddlybear.Bot.extensions.HypixelExtension
import me.thecuddlybear.Bot.extensions.MusicExtension
import me.thecuddlybear.Bot.extensions.SlapExtension
import org.dotenv.vault.dotenvVault

val dotenv = dotenvVault()
private val TOKEN = dotenv["TOKEN"]

lateinit var lavalink: LavaKord


val listeners = mutableMapOf<String, String>()
val queues = mutableMapOf<String,ArrayDeque<Track>>()

suspend fun main() {

    val bot = ExtensibleBot(TOKEN){
        extensions {
            add(::SlapExtension)
            add(::MusicExtension)
            add(::HypixelExtension)
            add(::AnimeActionsExtension)
        }

        presence {
            status = PresenceStatus.DoNotDisturb

            watching("Ruiz hacking")
        }

        applicationCommands {
            defaultGuild(dotenv["GUILD"])
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

    lavalink = bot.kordRef.lavakord {
        plugins {
            install(LavaSrc)
            install(Sponsorblock)
        }
    }

    lavalink.addNode(dotenv["LAVALINK_URL"], dotenv["LAVALINK_PASS"])

    bot.start()

}
