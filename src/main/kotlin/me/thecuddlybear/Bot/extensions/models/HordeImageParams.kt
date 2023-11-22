package me.thecuddlybear.Bot.extensions.models

import kotlinx.serialization.Serializable

/**
 * Represents the parameters for generating a Horde image.
 *
 * @property sampler_name The name of the sampler to be used for generating the image. Default value is "k_lms".
 * @property cfg_scale The scale factor for the image configuration. Default value is 7.5.
 * @property denoising_strength The strength of the denoising applied to the image.
 * @property seed The seed value used for random number generation. Default value is an empty string.
 * @property height The height of the generated image. Default value is 512.
 * @property width The width of the generated image. Default value is 512.
 * @property seed_variation The amount of variation to apply to the seed value.
 * @property post_processing An array of strings representing the post-processing steps to be applied to the image. Default value is an empty array.
 * @property karras Indicates whether Karras-style post-processing should be applied to the image. Default value is false.
 * @property tiling Indicates whether the image should be tiled. Default value is false.
 * @property hires_fix Indicates whether to apply a fix for high-resolution images. Default value is false.
 * @property clip_skip The number of steps to skip for clip. Default value is not specified.
 * @property control_type The type of control to be used for generating the image. Default value is an empty string.
 * @property image_is_control Indicates whether the seed value represents a control image. Default value is false.
 * @property return_control_map Indicates whether to return the control map. Default value is false.
 * @property facefixer_strength The strength of the face fixer applied to the image.
 * @property steps The number of steps to use for generating the image. Default value is 30.
 * @property n The value of n for the image generation. Default value is 1.
 */
@Serializable
data class HordeImageParams(
    val sampler_name: String = "k_lms",
    val cfg_scale: Double = 7.5,
    val denoising_strength: Double,
    val seed: String = "",
    val height: Int = 512,
    val width: Int = 512,
    val seed_variation: Int,
    val post_processing: Array<String> = arrayOf(),
    val karras: Boolean = false,
    val tiling: Boolean = false,
    val hires_fix: Boolean = false,
    val clip_skip: Int,
    val image_is_control: Boolean = false,
    val return_control_map: Boolean = false,
    val facefixer_strength: Double,
    val steps: Int = 30,
    val n: Int = 1
)
