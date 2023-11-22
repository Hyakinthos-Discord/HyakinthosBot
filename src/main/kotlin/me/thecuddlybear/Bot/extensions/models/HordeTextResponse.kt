package me.thecuddlybear.Bot.extensions.models

import kotlinx.serialization.Serializable
import me.thecuddlybear.Bot.extensions.models.HordeTextResponseGeneration

/**
 * Represents a response received from the Horde text generation service.
 *
 * @param generations An array of HordeTextResponseGeneration objects representing the generated texts.
 * @param finished The number of text generation processes that have finished.
 * @param processing The number of text generation processes that are currently being processed.
 * @param restarted The number of text generation processes that have been restarted.
 * @param waiting The number of text generation processes that are waiting in the queue.
 * @param done A boolean indicating whether all text generation processes have finished.
 * @param faulted A boolean indicating whether any error occurred during the text generation process.
 * @param wait_time The estimated wait time in seconds for the current text generation process.
 * @param queue_position The position in the queue of the current text generation process.
 * @param kudos The kudos score of the generated text.
 * @param is_possible A boolean indicating whether the text generation is possible given the input data.
 */
@Serializable
data class HordeTextResponse(
    val generations: Array<HordeTextResponseGeneration>,
    val finished: Int,
    val processing: Int,
    val restarted: Int,
    val waiting: Int,
    val done: Boolean,
    val faulted: Boolean,
    val wait_time: Int,
    val queue_position: Int,
    val kudos: Double,
    val is_possible: Boolean
)