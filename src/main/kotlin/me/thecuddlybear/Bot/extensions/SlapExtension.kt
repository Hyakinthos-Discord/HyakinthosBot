package me.thecuddlybear.Bot.extensions

import com.kotlindiscord.kord.extensions.extensions.Extension
import com.kotlindiscord.kord.extensions.extensions.publicMessageCommand
import com.kotlindiscord.kord.extensions.extensions.publicSlashCommand
import dev.kord.cache.api.data.description

class SlapExtension : Extension() {

    override val name: String = "slap"

    override suspend fun setup() {
        publicSlashCommand {
            name = "slap"
            description = "Get slapped!"

            action {
                respond {
                    content = "_Slaps ${user.mention} around a bit with a smelly trout!_"
                }
            }
        }

    }

}