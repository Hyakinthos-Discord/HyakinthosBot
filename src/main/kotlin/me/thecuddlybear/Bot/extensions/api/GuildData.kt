package me.thecuddlybear.Bot.extensions.api

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class GuildData(
    val id: Long,
    val created_at: Long
)