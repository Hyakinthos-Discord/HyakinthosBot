package me.thecuddlybear.Bot.extensions.models

import kotlinx.serialization.Serializable

/**
 * Represents a payload for the Horde Image API.
 *
 * This class holds the necessary data for performing image generation using the Horde Image API.
 *
 * @property prompt The prompt for generating the image.
 * @property params The parameters for controlling the image generation process.
 * @property nsfw A flag indicating whether the generated image can contain NSFW content. Defaults to false.
 * @property trusted_workers A flag indicating whether to use trusted workers for generating the image. Defaults to false.
 * @property slow_workers A flag indicating whether to use slow workers for generating the image. Defaults to true.
 * @property censor_nsfw A flag indicating whether to censor NSFW content in the generated image. Defaults to false.
 * @property workers An array of worker IDs to use for generating the image.
 * @property workers_blacklist A flag indicating whether to exclude workers in the `workers` array from generating the image. Defaults to false.
 * @property models An array of model IDs to use for generating the image.
 * @property source_image The source image to use for conditioning the image generation process. Nullable.
 * @property source_processing The source image processing method to use for conditioning the image generation process. Nullable.
 * @property source_mask The source image mask to use for conditioning the image generation process. Nullable.
 * @property r2 A flag indicating whether to use the R2 method for generating the image. Defaults to true.
 * @property shared A flag indicating whether the generated image can be viewed and shared publicly. Defaults to false.
 * @property replacement_filter A flag indicating whether to enable replacement filter for the generated image. Defaults to true.
 * @property dry_run A flag indicating whether to perform a dry run of the image generation process. Defaults to false.
 */
@Serializable
data class HordeImagePayload(
    val prompt: String,
    val params: HordeImageParams,
    val nsfw: Boolean = false,
    val trusted_workers: Boolean = false,
    val slow_workers: Boolean = true,
    val censor_nsfw: Boolean = false,
    val workers: Array<String> = arrayOf(),
    val workers_blacklist: Boolean = false,
    val models: Array<String>,
    val r2: Boolean = true,
    val shared: Boolean = false,
    val replacement_filter: Boolean = true,
    val dry_run: Boolean = false
)
