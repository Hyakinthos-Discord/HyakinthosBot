package me.thecuddlybear.Bot.extensions.api

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class MemberData(
    val id: Long,
    val username: String,
    val created_at: Long? = null
)

@Serializable
data class MemberLevelData(
    val id: Long,
    val xp: Long,
    val level: Long,
    val updated: Long? = 0,
)
