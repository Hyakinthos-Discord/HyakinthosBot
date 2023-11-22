package me.thecuddlybear.Bot.extensions.models

import kotlinx.serialization.Serializable

/**
 * Represents the response received when querying the HordeImage
 *
 * @property generations An array of HordeImageResponseGeneration objects representing the image generations
 * @property finished The number of generations that have finished processing
 * @property processing The number of generations currently being processed
 * @property restarted The number of generations that have been restarted
 * @property waiting The number of generations waiting in the queue
 * @property done Indicates if the processing of all generations is completed
 * @property faulted Indicates if any generation encountered an error during processing
 * @property wait_time The estimated time to wait before the next generation starts processing
 * @property queue_position The position of the current generation in the processing queue
 * @property kudos A measure of the goodness of the image
 * @property is_possible Indicates if it is possible to generate an image given the input parameters
 */
@Serializable
data class HordeImageResponse(
    val generations: Array<HordeImageResponseGeneration>,
    val finished: Int,
    val processing: Int,
    val restarted: Int,
    val waiting: Int,
    val message: String = "",
    val done: Boolean,
    val shared: Boolean = false,
    val faulted: Boolean,
    val wait_time: Int,
    val queue_position: Int,
    val kudos: Double,
    val is_possible: Boolean
)
