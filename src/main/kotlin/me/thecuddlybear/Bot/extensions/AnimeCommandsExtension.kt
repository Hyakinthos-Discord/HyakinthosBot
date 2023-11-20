package me.thecuddlybear.Bot.extensions

import com.kotlindiscord.kord.extensions.commands.Arguments
import com.kotlindiscord.kord.extensions.commands.converters.impl.user
import com.kotlindiscord.kord.extensions.extensions.Extension
import com.kotlindiscord.kord.extensions.extensions.publicSlashCommand
import dev.kord.rest.builder.message.create.embed
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.datetime.Clock

class AnimeCommandsExtension : Extension() {

    override val name: String = "AnimeCommands"

    val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json()
        }
    }

    override suspend fun setup() {
        publicSlashCommand(::AnimeArguments) {
            name = "slap"
            description = "Slaps the chosen user"

            action {
                val response: HttpResponse = client.request("https://api.otakugifs.xyz/gif?reaction=slap")
                val obj: JsonResponse = response.body()

                respond {
                    embed {
                        description = "${user.mention} slapped ${arguments.usr.mention}}"
                        timestamp = Clock.System.now()
                        image = obj.url
                        author{
                            name = "${member?.asMember()?.effectiveName} (${user.asUser().globalName})"
                            icon = user.asUser().avatar?.cdnUrl?.toUrl()
                        }
                    }
                }
            }
        }
    }

    inner class AnimeArguments : Arguments() {
        val usr by user {
            name = "user"
            description = "User to perform action on"
        }
    }

}