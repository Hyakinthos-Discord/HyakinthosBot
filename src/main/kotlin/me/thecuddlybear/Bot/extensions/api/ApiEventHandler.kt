package me.thecuddlybear.Bot.extensions.api

import com.kotlindiscord.kord.extensions.extensions.Extension
import com.kotlindiscord.kord.extensions.extensions.event
import dev.kord.common.entity.ChannelType
import dev.kord.common.entity.Snowflake
import dev.kord.core.behavior.channel.createMessage
import dev.kord.core.entity.channel.TextChannel
import dev.kord.core.event.guild.GuildCreateEvent
import dev.kord.core.event.guild.GuildDeleteEvent
import dev.kord.core.event.guild.MemberJoinEvent
import dev.kord.core.event.message.MessageCreateEvent
import dev.kord.gateway.GuildMemberAdd
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.supabaseJson
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.long
import me.thecuddlybear.Bot.supaClient

class ApiEventHandler : Extension() {

    private fun xpToLevel(xp: Long): Long {
        return (0.07 * Math.sqrt(xp.toDouble())).toLong()
    }

    private fun levelToXp(level: Long): Long {
        return Math.pow((level/0.07), 2.0).toLong()
    }

    override val name: String
        get() = "Api Events"

    override suspend fun setup() {
        event<MemberJoinEvent> {
            action {
                if (!event.member.isBot){
                    val username = event.member.username
                    val id = event.member.id.toString().toLong()

                    val member = MemberData(id = id, username = username, created_at = Clock.System.now().toEpochMilliseconds())

                    supaClient.postgrest["user"].insert(member, upsert = true)
                }
            }
        }

        event<MessageCreateEvent> {
            action {
                if(event.message.channel.asChannel().type != ChannelType.DM && !event.member!!.isBot){
                    val content = event.message.content
                    val length = content.length
                    val xp = (length*0.23).toLong()
                    var announceChannel: Snowflake? = event.getGuildOrNull()?.systemChannelId

                    val guildConfig: JsonObject? = supaClient.postgrest["guild"].select {
                        GuildData::id eq event.guildId.toString().toLong()
                    }.decodeSingle<GuildData>().config

                    if(guildConfig != null){
                        val string = guildConfig["announceChannelId"].toString().removePrefix("\"").removeSuffix("\"")
                        announceChannel = Snowflake(string.toLong())
                    }

                    var lvlData: MemberLevelData

                    val userData = supaClient.postgrest["user"].select {
                        MemberData::id eq event.message.author?.id.toString().toLong()
                    }.decodeList<MemberData>()

                    val levelData = supaClient.postgrest["level"].select {
                        MemberLevelData::id eq event.message.author?.id.toString().toLong() + event.guildId.toString().toLong() - event.guildId.toString().first().code.toLong()
                    }.decodeList<MemberLevelData>()

                    if (userData.isEmpty()){
                        val username = event.message.author!!.username
                        val id = event.message.author!!.id.toString().toLong()

                        val member = MemberData(id = id, username = username, created_at = Clock.System.now().toEpochMilliseconds())

                        supaClient.postgrest["user"].insert(member, upsert = true)
                    }

                    if(levelData.isEmpty()){
                        lvlData = MemberLevelData(id = event.message.author?.id.toString().toLong() + event.guildId.toString().toLong() - event.guildId.toString().first().code.toLong(), xp = 0, level = 0)
                        supaClient.postgrest["level"].insert(lvlData, upsert = true)
                    }else{
                        val prevData = levelData.first()
                        val update = event.message.timestamp.toEpochMilliseconds()
                        if (Math.abs(prevData.updated!! - update) > 12000 && xp > 0){
                            val level = xpToLevel((prevData.xp+xp))

                            if(level > prevData.level){
                                if (announceChannel != null) {
                                    val chann: TextChannel = event.getGuildOrNull()?.getChannel(announceChannel)?.asChannel() as TextChannel
                                    chann.createMessage("${event.message.author?.mention} has leveled up! ${prevData.level} ➜ ${level}")
                                }
                            }

                            val lvlData = MemberLevelData(id = event.message.author?.id.toString().toLong() + event.guildId.toString().toLong() - event.guildId.toString().first().code.toLong(), xp = prevData.xp+xp, level = level, updated=update)
                            supaClient.postgrest["level"].insert(lvlData, upsert = true)
                        }
                    }
                }
            }
        }


        event<GuildCreateEvent> {
            action {
                val guildId = event.guild.id.toString().toLong()

                val guildModel = GuildData(id = guildId)

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