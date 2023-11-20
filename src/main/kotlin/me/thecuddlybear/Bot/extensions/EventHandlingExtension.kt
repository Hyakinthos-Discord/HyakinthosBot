package me.thecuddlybear.Bot.extensions

import com.kotlindiscord.kord.extensions.extensions.Extension
import com.kotlindiscord.kord.extensions.extensions.event
import dev.kord.core.behavior.reply
import dev.kord.core.event.message.MessageCreateEvent

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
            }
        }
    }

}