package me.thecuddlybear.Bot.extensions

import com.kotlindiscord.kord.extensions.commands.Arguments
import com.kotlindiscord.kord.extensions.commands.application.slash.publicSubCommand
import com.kotlindiscord.kord.extensions.commands.converters.impl.string
import com.kotlindiscord.kord.extensions.extensions.Extension
import com.kotlindiscord.kord.extensions.extensions.publicSlashCommand
import de.snowii.MojangAPI
import me.thecuddlybear.Bot.dotenv
import net.hypixel.api.HypixelAPI
import net.hypixel.api.apache.ApacheHttpClient
import net.hypixel.api.reply.PlayerReply
import java.util.*
import java.util.concurrent.CompletableFuture


class HypixelExtension : Extension() {

    private val hypixel = HypixelAPI(ApacheHttpClient(UUID.fromString(dotenv["HYPIXEL"])))
    private val mojang = MojangAPI()

    override val name: String = "hypixel"
    override suspend fun setup() {
        publicSlashCommand{
            name = "hypixel"
            description = "Get hypixel stats!"

            publicSubCommand(::HypixelArguments)  {
                name = "player"
                description = "Get's the player network stats"

                action {
                    val player: PlayerReply? = hypixel.getPlayerByUuid(MojangAPI.getGameProfile(arguments.playerName)?.uuid).get()

                    respond{
                        if (player != null) {
                            content = "The level of that player is ${player.player.networkLevel}"
                        }else{
                            content = "That player doesn't exist!"
                        }
                    }
                }

            }

        }
    }

    inner class HypixelArguments : Arguments() {
        val playerName by string {
            name = "username"
            description = "Username of the player you want to get"
        }
    }

}