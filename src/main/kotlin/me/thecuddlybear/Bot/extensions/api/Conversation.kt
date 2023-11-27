package me.thecuddlybear.Bot.extensions.api

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class Conversation(
    val thread_id: Long,
    val guild_id: Long,
    val prompt: String,
    val created_at: Long
)
