package me.thecuddlybear.Bot.extensions.models

import kotlinx.serialization.Serializable

/**
 *
 * Represents the parameters to customize the behavior of the HordeText class.
 *
 * @property n The number of texts to generate.
 * @property max_context_length The maximum length of the context used by the model.
 * @property max_length The maximum length of the generated text.
 * @property frmttriminc Whether to incrementally trim formatting for each candidate text.
 * @property rep_pen The repetition penalty applied during text generation.
 * @property rep_pen_range The range within which the repetition penalty is applied.
 * @property rep_pen_slope The slope of the repetition penalty function.
 * @property singleline Whether to generate single line texts only.
 * @property temperature The temperature parameter controlling the randomness of the generated text.
 * @property tfs The task-specific constants for text generation.
 * @property top_a The number of tokens to include in the prompt when using the top-k sampling method.
 * @property top_k The number of top tokens to sample from when using the top-k sampling method.
 * @property top_p The cumulative probability threshold for top-p sampling.
 * @property typical The typical number of tokens in a generated text.
 * @property sampler_order The order of samplers to use during text generation.
 * @property use_default_badwordsids Whether to use default banned word IDs during text generation.
 * @property stop_sequence The stop sequences to indicate the end of text generation.
 */
@Serializable
data class HordeTextParams(
    val n: Int,
    val max_context_length: Int,
    val max_length: Int,
    val frmttriminc: Boolean = true,
    val rep_pen: Double,
    val rep_pen_range: Int,
    val rep_pen_slope: Double,
    val singleline: Boolean,
    val temperature: Double,
    val tfs: Int,
    val top_a: Int,
    val top_k: Int,
    val top_p: Double,
    val typical: Int,
    val sampler_order: Array<Int>,
    val use_default_badwordsids: Boolean,
    val stop_sequence: Array<String>
)