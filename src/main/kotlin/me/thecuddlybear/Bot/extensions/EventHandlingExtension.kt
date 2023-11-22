package me.thecuddlybear.Bot.extensions

import com.kotlindiscord.kord.extensions.extensions.Extension
import com.kotlindiscord.kord.extensions.extensions.event
import com.kotlindiscord.kord.extensions.extensions.publicSlashCommand
import dev.kord.core.behavior.interaction.followup.edit
import dev.kord.core.behavior.reply
import dev.kord.core.event.message.MessageCreateEvent
import dev.kord.rest.builder.message.create.FollowupMessageCreateBuilder

class EventHandlingExtension : Extension() {

    override val name: String
        get() = "EventHandler"

    override suspend fun setup() {
        event<MessageCreateEvent> {
            action {
                if(event.message.content == "skibidi"){
                    event.message.reply {
                        content = "toilet"
                    }
                }
                if(event.message.content.lowercase().contains("ruiz")){
                    event.message.reply {
                        content = "ruhizzz"
                    }
                }
            }
        }
    }

}