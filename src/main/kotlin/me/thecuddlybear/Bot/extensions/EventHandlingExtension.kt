package me.thecuddlybear.Bot.extensions

import com.kotlindiscord.kord.extensions.extensions.Extension
import com.kotlindiscord.kord.extensions.extensions.event
import com.kotlindiscord.kord.extensions.extensions.publicSlashCommand
import dev.kord.common.entity.ArchiveDuration
import dev.kord.core.behavior.interaction.followup.edit
import dev.kord.core.behavior.reply
import dev.kord.core.entity.channel.TextChannel
import dev.kord.core.event.message.MessageCreateEvent
import dev.kord.rest.builder.message.create.FollowupMessageCreateBuilder

class EventHandlingExtension : Extension() {

    override val name: String
        get() = "EventHandler"

    override suspend fun setup() {
        event<MessageCreateEvent> {
            action {
                if(event.member != null{
                    if(!event.member?.isBot!!){
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
                        if(event.message.content.lowercase().contains("caption")){
                            event.message.reply {
                                content = "cola"
                            }
                        }
                        if(event.message.content.lowercase().contains("ben")){
                            event.message.reply {
                                content = "Ben????????"
                            }
                        }
                    }
                }
            }
        }

        publicSlashCommand {
            name = "createThread"
            description = "Creates a thread"

            action {
                val hello = respond {
                    content = "Okay! Creating a thread"
                }

                val channel: TextChannel = hello.channel.asChannel() as TextChannel

                channel.startPublicThreadWithMessage(hello.id, "Hello") {
                    name = "New Thread :D"
                    autoArchiveDuration = ArchiveDuration.Hour
                }

            }
        }
    }

}
