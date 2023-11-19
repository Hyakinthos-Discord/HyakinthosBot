package me.thecuddlybear.Bot.extensions

import com.kotlindiscord.kord.extensions.commands.Arguments
import com.kotlindiscord.kord.extensions.commands.application.slash.converters.impl.defaultingStringChoice
import com.kotlindiscord.kord.extensions.commands.application.slash.ephemeralSubCommand
import com.kotlindiscord.kord.extensions.commands.application.slash.publicSubCommand
import com.kotlindiscord.kord.extensions.commands.converters.impl.string
import com.kotlindiscord.kord.extensions.extensions.Extension
import com.kotlindiscord.kord.extensions.extensions.publicSlashCommand
import de.snowii.MojangAPI
import dev.kord.common.entity.DiscordEmbed
import dev.kord.rest.builder.message.EmbedBuilder
import dev.kord.rest.builder.message.create.embed
import me.thecuddlybear.Bot.dotenv
import net.hypixel.api.HypixelAPI
import net.hypixel.api.apache.ApacheHttpClient
import net.hypixel.api.reply.PlayerReply
import java.util.*
import java.util.concurrent.CompletableFuture
import kotlin.math.floor
import kotlin.math.round


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
                        if(player != null){
                            embed {
                                title = "${arguments.playerName}'s Network Stats"
                                author {
                                    name = "${member?.asMember()?.effectiveName} (${user.asUser().globalName})"
                                    icon = "https://avatars.githubusercontent.com/u/3840546?s=280&v=4"
                                }
                                thumbnail {
                                    url = "https://cravatar.eu/head/${arguments.playerName}"
                                }
                                field{
                                    name = "Level"
                                    value = "${round(player.player.networkLevel)}"
                                    inline = true
                                }
                                field{
                                    name = "Experience"
                                    value = "${player.player.networkExp}"
                                    inline = true
                                }
                                field{
                                    name = "Karma"
                                    value = "${player.player.karma}"
                                    inline = true
                                }
                            }
                        }else{
                            respond { content = "That player does not exist!"}

                        }
                    }
                }

            }

            publicSubCommand(::BedwarsArguments) {
                name = "bedwars"
                description = "Get's bedwars stats"


                action {
                    val player: PlayerReply? = hypixel.getPlayerByUuid(MojangAPI.getGameProfile(arguments.plauer)?.uuid).get()

                    if(player != null){
                        val bedwarsStats = player.player.getArrayProperty("stats.Bedwars")

                        when(arguments.whichBedwars){
                            "gen" -> respond {
                                embed {
                                    title = "${arguments.plauer}'s Bedwars Stats"
                                    description = "These are the total bedwars stats"
                                    author {
                                        name = "${member?.asMember()?.effectiveName} (${user.asUser().globalName})"
                                        icon = "https://static.wikia.nocookie.net/minecraft/images/c/c5/Bed.png/revision/latest?cb=20191103220226"
                                    }
                                    thumbnail {
                                        url = "https://cravatar.eu/head/${arguments.plauer}"
                                    }
                                    field{
                                        name = "Coins"
                                        value = player.player.getLongProperty("stats.Bedwars.coins", 0).toString()
                                    }
                                    field{
                                        name = "Winstreak"
                                        value = player.player.getIntProperty("stats.Bedwars.winstreak", 0).toString()
                                    }

                                    //Inline
                                    field{
                                        name = "Diamonds Collected"
                                        value = player.player.getLongProperty("stats.Bedwars.diamond_resources_collected_bedwars", 0).toString()
                                        inline = true
                                    }
                                    field{
                                        name = "Emeralds Collected"
                                        value = player.player.getLongProperty("stats.Bedwars.emerald_resources_collected_bedwars", 0).toString()
                                        inline = true
                                    }
                                    field{
                                        name = "Iron Collected"
                                        value = player.player.getLongProperty("stats.Bedwars.iron_resources_collected_bedwars", 0).toString()
                                        inline = true
                                    }
                                    field{
                                        name = "Gold Collected"
                                        value = player.player.getLongProperty("stats.Bedwars.gold_resources_collected_bedwars", 0).toString()
                                        inline = true
                                    }
                                }
                                embed{
                                    field{
                                        name = "Normal kills"
                                        value = player.player.getLongProperty("stats.Bedwars.kills_bedwars", 0).toString()
                                        inline = true
                                    }
                                    field{
                                        name = "Normal Deaths"
                                        value = player.player.getLongProperty("stats.Bedwars.deaths_bedwars", 0).toString()
                                        inline = true
                                    }
                                }
                            }
                        }
                    }else{
                        respond{
                            content = "Player does not exist!"
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


    inner class BedwarsArguments: Arguments() {
        val plauer by string {
            name = "username"
            description = "Username of the player you want to get"
        }

        val whichBedwars by defaultingStringChoice {
            name = "gamemode"
            description = "Which type of gamemode you want to see the stats of, leave empty for total stats"
            defaultValue = "gen"

            choices["Singles"] = "eight_one"
            choices["Doubles"] = "eight_two"
            choices["3v3v3v3"] = "four_three"
            choices["4v4v4v4"] = "four_four"
        }
    }

}