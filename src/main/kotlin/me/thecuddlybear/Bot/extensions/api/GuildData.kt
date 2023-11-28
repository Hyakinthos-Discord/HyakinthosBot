package me.thecuddlybear.Bot.extensions.api

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

@Serializable
data class GuildData(
    val id: Long,
    val config: JsonObject? = null,
)