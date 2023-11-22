package me.thecuddlybear.Bot.extensions.models

import kotlinx.serialization.Serializable

/**
 * Represents a response from the Horde Image Generation API.
 *
 * This class is annotated with '@Serializable' to enable serialization and deserialization of objects
 * of this class using a serialization framework such as Kotlinx Serialization or Jackson.
 *
 * @property censored Boolean indicating whether the generated image is censored or not.
 * @property gen_metadata Array of strings containing generated image metadata.
 * @property id String identifier for the generated image.
 * @property img String representing the generated image (encoded as a base64 string, for example).
 * @property model String indicating the model used for image generation.
 * @property seed String representing the seed provided for image generation.
 * @property state String indicating the state of the generation process.
 * @property worker_id String identifier for the worker that generated the image.
 * @property worker_name String name of the worker that generated the image.
 */
@Serializable
data class HordeImageResponseGeneration(
    val censored: Boolean,
    val gen_metadata: Array<String>,
    val id: String,
    val img: String,
    val model: String,
    val seed: String,
    val state: String,
    val worker_id: String,
    val worker_name: String
)