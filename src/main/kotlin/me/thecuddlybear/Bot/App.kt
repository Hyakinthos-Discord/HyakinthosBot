package me.thecuddlybear.Bot

import com.kotlindiscord.kord.extensions.ExtensibleBot
import dev.arbjerg.lavalink.protocol.v4.Track
import dev.kord.common.entity.PresenceStatus
import dev.kord.gateway.Intent
import dev.kord.gateway.PrivilegedIntent
import dev.schlaubi.lavakord.LavaKord
import dev.schlaubi.lavakord.kord.lavakord
import dev.schlaubi.lavakord.plugins.lavasrc.LavaSrc
import dev.schlaubi.lavakord.plugins.sponsorblock.Sponsorblock
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.GoTrue
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.realtime.Realtime
import me.thecuddlybear.Bot.extensions.*
import me.thecuddlybear.Bot.extensions.api.ApiEventHandler
import org.dotenv.vault.dotenvVault

val dotenv = dotenvVault()
private val TOKEN = dotenv["TOKEN"]

lateinit var lavalink: LavaKord

val listeners = mutableMapOf<String, String>()
val queues = mutableMapOf<String,ArrayDeque<Track>>()

val supaClient = createSupabaseClient(
    supabaseKey = dotenv["SUPA_ANON"],
    supabaseUrl = dotenv["SUPA_URL"]
) {
    install(GoTrue)
    install(Postgrest)
    install(Realtime)
}

/**
 * This is the main class for the application.
 * It contains the main function which is the entry point of the application.
 */
suspend fun main() {

    val bot = ExtensibleBot(TOKEN){
        // Add bot extensions
        extensions {
            add(::MusicExtension)
            add(::HypixelExtension)
            add(::AnimeActionsExtension)
            add(::AnimeCommandsExtension)
            add(::EventHandlingExtension)
            add(::AiExtension)
            add(::FunExtension)
            add(::ApiEventHandler)
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
            enabled = false
            invokeOnMention = true
        }

        @OptIn(PrivilegedIntent::class)
        intents {
            +Intent.GuildMembers
            +Intent.MessageContent
            +Intent.GuildMessages
        }
    }

    // Initialize lavalink for music
    lavalink = bot.kordRef.lavakord {
        plugins {
            install(LavaSrc)
            install(Sponsorblock)
        }
    }

    // Add the node given in the config
    lavalink.addNode(dotenv["LAVALINK_URL"], dotenv["LAVALINK_PASS"])

    bot.start()

}
