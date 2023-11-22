package me.thecuddlybear.Bot.extensions.models

import kotlinx.serialization.Serializable

/**
 * Represents a text payload for Horde.
 *
 * @property prompt The main prompt for generating text.
 * @property params Additional parameters for generating text.
 * @property softprompt A soft prompt that provides context or instructions to the model.
 * @property trusted_workers Indicates whether to use trusted workers for generation.
 * @property slow_workers Indicates whether to prioritize slower workers for generation.
 * @property workers The list of worker IDs to use for generation.
 * @property worker_blacklist Indicates whether to blacklist certain workers for generation.
 * @property models The list of model names or IDs to use for generation.
 * @property dry_run Indicates whether to perform a dry run without generating text.
 */
@Serializable
data class HordeTextPayload(
    val prompt: String,
    val params: HordeTextParams,
    val softprompt: String,
    val trusted_workers: Boolean,
    val slow_workers: Boolean,
    val workers: Array<String>,
    val worker_blacklist: Boolean,
    val models: Array<String>,
    val dry_run: Boolean
)