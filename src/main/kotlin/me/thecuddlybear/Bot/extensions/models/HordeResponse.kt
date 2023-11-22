package me.thecuddlybear.Bot.extensions.models

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject

/**
 * Represents a response from the Horde system.
 *
 * @property id The unique identifier of the response.
 * @property kudos The kudos score associated with the response.
 * @property message The message content of the response.
 */
@Serializable
data class HordeResponse(
    val id: String = "",
    val kudos: Double = 0.0,
    val message: String = "",
    val errors: JsonObject = Json.parseToJsonElement("{}").jsonObject
)