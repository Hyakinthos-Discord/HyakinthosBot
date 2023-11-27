package me.thecuddlybear.Bot.extensions.api

import com.kotlindiscord.kord.extensions.extensions.Extension
import com.kotlindiscord.kord.extensions.extensions.event
import dev.kord.core.event.guild.GuildCreateEvent
import dev.kord.core.event.guild.GuildDeleteEvent
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import me.thecuddlybear.Bot.supaClient

class ApiEventHandler : Extension() {

    override val name: String
        get() = "Api Events"

    override suspend fun setup() {
        event<GuildCreateEvent> {
            action {
                val guildId = event.guild.id.toString().toLong()

                val guildModel = GuildData(guildId, Clock.System.now().toEpochMilliseconds())

                supaClient.postgrest["guild"].insert(guildModel, upsert = true)
            }
        }

        event<GuildDeleteEvent> {
            action {
                val guildId = event.guild?.id.toString().toLong()

                supaClient.postgrest["guild"].delete {
                    GuildData::id eq guildId
                }

            }
        }
    }

}