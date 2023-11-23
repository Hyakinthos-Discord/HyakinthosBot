package me.thecuddlybear.Bot.extensions

import com.kotlindiscord.kord.extensions.commands.application.slash.publicSubCommand
import com.kotlindiscord.kord.extensions.extensions.Extension
import com.kotlindiscord.kord.extensions.extensions.publicSlashCommand
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject

class FunExtension : Extension() {

    override val name: String
        get() = "Fun"

    val client: HttpClient = HttpClient(CIO){
        install(ContentNegotiation){
            json()
        }
    }


    override suspend fun setup() {

        publicSlashCommand{
            name = "facts"
            description = "Gives a random fact"

            publicSubCommand {
                name = "dog"
                description = "Gives a random dog fact"

                action {

                    val response: HttpResponse = client.get("https://dog-api.kinduff.com/api/facts")
                    val body: JsonObject = response.body()
                    val facts: JsonArray = body["facts"] as JsonArray

                    respond {
                        content = "Sure here's a random fact: `${facts.first().toString()}`"
                    }

                }

            }

            publicSubCommand {
                name = "cat"
                description = "Gives a random cat fact"

                action {

                    val response: HttpResponse = client.get("https://meowfacts.herokuapp.com/")
                    val body: JsonObject = response.body()
                    val facts: JsonArray = body["data"] as JsonArray

                    respond {
                        content = "Sure here's a random fact: `${facts.first().toString()}`"
                    }

                }

            }

        }


    }

}