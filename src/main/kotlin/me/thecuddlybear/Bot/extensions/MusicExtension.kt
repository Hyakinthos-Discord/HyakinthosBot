package me.thecuddlybear.Bot.extensions

import com.kotlindiscord.kord.extensions.commands.Arguments
import com.kotlindiscord.kord.extensions.commands.converters.impl.string
import com.kotlindiscord.kord.extensions.extensions.Extension
import com.kotlindiscord.kord.extensions.extensions.publicSlashCommand
import dev.arbjerg.lavalink.protocol.v4.LoadResult
import dev.arbjerg.lavalink.protocol.v4.Track
import dev.kord.common.entity.Snowflake
import dev.kord.core.behavior.channel.asChannelOf
import dev.kord.core.behavior.channel.createMessage
import dev.kord.core.entity.channel.TextChannel
import dev.schlaubi.lavakord.audio.*
import dev.schlaubi.lavakord.audio.player.guildId
import dev.schlaubi.lavakord.rest.loadItem
import io.ktor.util.reflect.*
import me.thecuddlybear.Bot.lavalink
import me.thecuddlybear.Bot.listeners
import me.thecuddlybear.Bot.queues
import org.koin.core.component.get

/**
 * Represents a music extension for a chat bot. This extension provides commands to play, connect, pause, skip, and stop music.
 */
class MusicExtension : Extension() {

    override val name: String = "music"

    /**
     * Sets up the commands for playing, pausing, skipping, and stopping songs in a voice channel.
     * This method should be called when the bot is initialized.
     */
    override suspend fun setup() {

        publicSlashCommand(::PlayArguments) {
            name = "play"
            description = "Play a song!"

            action{
                val voiceState = member?.getVoiceState()
                val channelId = voiceState?.channelId.toString()

                val link = lavalink.getLink(guild?.id.toString())
                val player = link.player

                if (!queues.containsKey(guild?.id.toString())){
                    listeners[guild?.id.toString()] = channel.id.toString()
                    queues[guild?.id.toString()] = ArrayDeque(0)

                    player.on<TrackStartEvent> {
                        val channelID = listeners[guild?.id.toString()]

                        val channel = bot.kordRef.getGuild(Snowflake(guildId)).getChannel(Snowflake(channelID.toString())).asChannelOf<TextChannel>()
                        channel.createMessage {
                            content = "Now playing: `${track.info.title} - ${track.info.author}`"
                        }

                    }

                    player.on<TrackEndEvent> {
                        if(queues[guildId.toString()]?.isNotEmpty() == true){
                            player.playTrack(track = queues[guildId.toString()]?.removeFirst() as Track)
                        }else {
                            link.disconnectAudio()
                        }
                    }
                }

                if(channelId == null){
                    respond{
                        content = "Please connect to a voice channel!"
                    }
                }

                listeners[guild?.id.toString()] = channel.id.toString()

                if (!queues.containsKey(guild?.id.toString())){
                    queues[guild?.id.toString()] = ArrayDeque(0)
                }

                if(link.state != Link.State.CONNECTED){
                    link.connect(channelId)
                }

                val query = arguments.url
                val search = if (query.startsWith("http")) {
                    query
                }else{
                    "ytsearch:$query"
                }

                when (val item = link.loadItem(search)) {
                    is LoadResult.TrackLoaded -> {
                        if(player.playingTrack == null){
                            player.playTrack(track = item.data)
                            respond { content = "Added `${item.data.info.title} - ${item.data.info.author}` to the queue!" }
                        }else{
                            queues[guild?.id.toString()]?.add(item.data)
                            respond { content = "Added `${item.data.info.title} - ${item.data.info.author}` to the queue!" }
                        }
                    }
                    is LoadResult.PlaylistLoaded -> {
                        queues[guild?.id.toString()]?.addAll(item.data.tracks)

                        if(player.playingTrack == null){
                            player.playTrack(track = queues[guild?.id.toString()]?.removeFirst() as Track)
                        }
                        respond { content = "Added the playlist to the queue!" }
                    }

                    is LoadResult.LoadFailed -> respond { content = item.data.message ?: "Exception" }
                    is LoadResult.NoMatches -> respond { content = "Could not find the song"}
                    is LoadResult.SearchResult -> {
                        if(player.playingTrack == null){
                            player.playTrack(track = item.data.tracks.first())
                            respond { content = "Playing the track" }
                        }else{
                            queues[guild?.id.toString()]?.add(item.data.tracks.first())
                            respond { content = "Added `${item.data.tracks.first().info.title} - ${item.data.tracks.first().info.author}` to the queue!" }
                        }
                    }
                }
            }
        }

        publicSlashCommand{
            name = "connect"
            description = "Connect to the voice channel"

            action {
                val voiceState = member?.getVoiceState()
                val channelId = voiceState?.channelId.toString()

                val link = lavalink.getLink(guild?.id.toString())
                val player = link.player

                if (!queues.containsKey(guild?.id.toString())){
                    listeners[guild?.id.toString()] = channel.id.toString()
                    queues[guild?.id.toString()] = ArrayDeque(0)

                    player.on<TrackStartEvent> {
                        val channelID = listeners[guild?.id.toString()]

                        val channel = bot.kordRef.getGuild(Snowflake(guildId)).getChannel(Snowflake(channelID.toString())).asChannelOf<TextChannel>()
                        channel.createMessage {
                            content = "Now playing: ```${track.info.title} - ${track.info.author}```"
                        }

                    }

                    player.on<TrackEndEvent> {
                        if(queues[guildId.toString()]?.isNotEmpty() == true){
                            player.playTrack(track = queues[guildId.toString()]?.removeFirst() as Track)
                        }
                    }
                }

                if(channelId == null){
                    respond{
                        content = "Please connect to a voice channel!"
                    }
                }

                if(link.state != Link.State.CONNECTED){
                    link.connect(channelId)
                    respond {
                        content = "I have connected to the voice channel!"
                    }
                }else{
                    respond { content = "I am already connected to a voice channel!" }
                }
            }
        }

        publicSlashCommand {
            name = "pause"
            description = "Pauses the current song!"

            action {
                val voiceState = member?.getVoiceState()
                val channelId = voiceState?.channelId.toString()

                val link = lavalink.getLink(guild?.id.toString())

                if(channelId == null){
                    respond{
                        content = "Please connect to a voice channel!"
                    }
                }

                val player = link.player

                player.pause(!player.paused)

                respond {
                    content = "Paused the song!"
                }

            }
        }

        publicSlashCommand {
            name = "skip"
            description = "Skips the current playing song!"

            action {
                val voiceState = member?.getVoiceState()
                val channelId = voiceState?.channelId.toString()

                val link = lavalink.getLink(guild?.id.toString())

                if(channelId == null){
                    respond{
                        content = "Please connect to a voice channel!"
                    }
                }

                val player = link.player
                player.stopTrack()

                respond {
                    content = "Stopped playing!"
                }

            }

        }

        publicSlashCommand {
            name = "stop"
            description = "Stops playing!"

            action {
                val voiceState = member?.getVoiceState()
                val channelId = voiceState?.channelId.toString()

                val link = lavalink.getLink(guild?.id.toString())

                if(channelId == null){
                    respond{
                        content = "Please connect to a voice channel!"
                    }
                }

                val player = link.player

                queues[guild?.id.toString()] = ArrayDeque<Track>(0)
                player.stopTrack()
                link.disconnectAudio()

                respond {
                    content = "Stopped playing!"
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