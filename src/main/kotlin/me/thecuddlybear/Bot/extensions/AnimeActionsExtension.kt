package me.thecuddlybear.Bot.extensions

import com.kotlindiscord.kord.extensions.extensions.Extension
import com.kotlindiscord.kord.extensions.extensions.publicUserCommand
import dev.kord.rest.builder.message.create.embed
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable


@Serializable
data class JsonResponse(val url: String)
class AnimeActionsExtension : Extension() {

    override val name: String = "anime"

    override suspend fun setup() {

        val client = HttpClient(CIO) {
            install(ContentNegotiation) {
                json()
            }
        }

        publicUserCommand {
            name = "Kiss"

            action{

                val response: HttpResponse = client.request("https://api.otakugifs.xyz/gif?reaction=kiss")
                val obj: JsonResponse = response.body()

                respond {
                    embed {
                        description = "${user.mention} kissed ${targetUsers.first().mention}"
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

        publicUserCommand {
            name = "Slap"

            action{

                val response: HttpResponse = client.request("https://api.otakugifs.xyz/gif?reaction=slap")
                val obj: JsonResponse = response.body()

                respond {
                    embed {
                        description = "${user.mention} slapped ${targetUsers.first().mention}"
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

        publicUserCommand {
            name = "Hug"

            action{

                val response: HttpResponse = client.request("https://api.otakugifs.xyz/gif?reaction=hug")
                val obj: JsonResponse = response.body()

                respond {
                    embed {
                        description = "${user.mention} hugged ${targetUsers.first().mention}"
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

        publicUserCommand {
            name = "Bite"

            action{

                val response: HttpResponse = client.request("https://api.otakugifs.xyz/gif?reaction=bite")
                val obj: JsonResponse = response.body()

                respond {
                    embed {
                        description = "${user.mention} bit ${targetUsers.first().mention}"
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

        publicUserCommand {
            name = "Smack"

            action{

                val response: HttpResponse = client.request("https://api.otakugifs.xyz/gif?reaction=smack")
                val obj: JsonResponse = response.body()

                respond {
                    embed {
                        description = "${user.mention} smacked ${targetUsers.first().mention}"
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

}