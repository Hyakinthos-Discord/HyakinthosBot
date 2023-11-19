package me.thecuddlybear.Bot.extensions

import com.kotlindiscord.kord.extensions.commands.Arguments
import com.kotlindiscord.kord.extensions.commands.converters.impl.coalescingDefaultingString
import com.kotlindiscord.kord.extensions.commands.converters.impl.coalescingString
import com.kotlindiscord.kord.extensions.commands.converters.impl.defaultingString
import com.kotlindiscord.kord.extensions.commands.converters.impl.string
import com.kotlindiscord.kord.extensions.extensions.Extension
import com.kotlindiscord.kord.extensions.extensions.chatCommand
import com.kotlindiscord.kord.extensions.extensions.publicSlashCommand
import com.kotlindiscord.kord.extensions.utils.respond
import com.ryanhcode.kopixel.KoPixelAPI.Companion.NewKoPixelAPI
import me.thecuddlybear.Bot.dotenv

class MusicExtension : Extension() {

    override val name: String = "music"

    override suspend fun setup() {
        publicSlashCommand(::PlayArguments) {
            name = "play"
            description = "Play a song!"

            action{
                respond{
                    content = "You want to play the song: ${arguments.url}"
                }
            }
        }
    }

    inner class PlayArguments : Arguments() {
        val url by string {
            name = "song"
            description = "Name or URL of the song you want to play"
        }
    }

}