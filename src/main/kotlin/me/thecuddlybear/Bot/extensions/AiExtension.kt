package me.thecuddlybear.Bot.extensions

import com.kotlindiscord.kord.extensions.commands.Arguments
import com.kotlindiscord.kord.extensions.commands.converters.impl.string
import com.kotlindiscord.kord.extensions.extensions.Extension
import com.kotlindiscord.kord.extensions.extensions.publicSlashCommand
import dev.kord.common.entity.ArchiveDuration
import dev.kord.core.behavior.channel.createMessage
import dev.kord.core.behavior.interaction.followup.edit
import dev.kord.core.entity.channel.TextChannel
import dev.kord.core.event.message.MessageCreateEvent
import io.ktor.client.*
import com.kotlindiscord.kord.extensions.extensions.event
import dev.kord.common.entity.Snowflake
import dev.kord.core.behavior.channel.withTyping
import io.github.jan.supabase.postgrest.postgrest
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.sentry.DateUtils
import kotlinx.coroutines.delay
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Serializable
import me.thecuddlybear.Bot.dotenv
import me.thecuddlybear.Bot.extensions.api.Conversation
import me.thecuddlybear.Bot.extensions.models.*
import me.thecuddlybear.Bot.supaClient
import java.time.format.DateTimeFormatter

/**
 * Represents an AI extension for a chat application.
 *
 * This class extends the `Extension` class and provides functionality
 * for interacting with an AI service.
 */
class AiExtension: Extension() {

    var threadMap: MutableMap<Snowflake, String> = mutableMapOf()

    override val name: String
        get() = "ai"

    val httpClient = HttpClient(CIO) {
        install(ContentNegotiation){
            json()
        }
    }

    override suspend fun setup() {

        event<MessageCreateEvent>{
            action {
                val messChannelId = event.message.channelId

                val threads = supaClient.postgrest["conversation"].select {
                    Conversation::thread_id eq messChannelId.toString().toLong()
                }.decodeList<Conversation>()


                if (threads.isNotEmpty() && !event.member?.isBot!!){
                    val channel = event.message.getChannel()
                    val prevPrompt = threads.first().prompt

                    channel.withTyping {
                        val prompt = prevPrompt + "\n### Instruction:\n${event.message.content}\n### Response:\n"
                        val textParam: HordeTextParams = HordeTextParams(
                            n = 1,
                            max_context_length = 1600,
                            max_length = 140,
                            rep_pen = 1.1,
                            rep_pen_range = 320,
                            rep_pen_slope = 0.7,
                            singleline = false,
                            temperature = 0.7,
                            tfs = 1,
                            top_a = 0,
                            top_k = 100,
                            top_p = 0.92,
                            typical = 1,
                            sampler_order = arrayOf(6, 0, 1, 3, 4, 2, 5),
                            use_default_badwordsids = true,
                            stop_sequence = arrayOf("### Instruction:", "### Response:")
                        )

                        val payload: HordeTextPayload = HordeTextPayload(
                            prompt = prompt,
                            params = textParam,
                            softprompt = "string",
                            trusted_workers = false,
                            slow_workers = true,
                            workers = arrayOf(),
                            worker_blacklist = false,
                            models = arrayOf("koboldcpp/OpenHermes-2.5-Mistral-7b", "aphrodite/Chronomaid-Storytelling-13b", "aphrodite/KoboldAI/LLaMA2-13B-Tiefighter", "KoboldAI/LLaMA2-13B-Tiefighter", "koboldcpp/mistrp-airoboros-7b", "koboldcpp/MythoLogic-Mini-7B"),
                            dry_run = false
                        )

                        val initialResponse : HttpResponse = httpClient.post("https://stablehorde.net/api/v2/generate/text/async") {
                            header("apikey", dotenv["AIHORDE"])
                            contentType(ContentType.Application.Json)
                            setBody(payload)
                        }


                        if (initialResponse.status.value.equals(202)){
                            val responseBody: HordeResponse = initialResponse.body()

                            var statusResponse : HttpResponse = httpClient.get("https://stablehorde.net/api/v2/generate/text/status/${responseBody.id}")
                            var statusData : HordeTextResponse = statusResponse.body()

                            do {
                                statusResponse = httpClient.get("https://stablehorde.net/api/v2/generate/text/status/${responseBody.id}")
                                statusData = statusResponse.body()
                                delay(1000)
                            }while (statusData.finished != 1)

                            var responseText = statusData.generations.first().text.removeSuffix("### Instruction:")
                            responseText = responseText.removeSuffix("### Response:")

                            createMessage(responseText)
                            val update: Conversation = Conversation(
                                thread_id = messChannelId.toString().toLong(),
                                guild_id = event.guildId.toString().toLong(),
                                prompt = prevPrompt + "\n### Instruction:\n${event.message.content}\n### Response:\n${responseText}",
                                created_at = threads.first().created_at
                            )
                            supaClient.postgrest["conversation"].insert(update, upsert = true)
                        }else{
                            val responseBody: HordeResponse = initialResponse.body()

                            createMessage("Error! ${responseBody.message}")
                        }
                    }

                }

            }
        }

        publicSlashCommand {
            name = "closeconversation"
            description = "End the conversation"

            action {
                val channelId = channel.id

                if (!threadMap.containsKey(channelId)){
                    respond {
                        content = "This is not a conversation thread/channel! Cannot close it"
                    }
                }else {
                    respond { content = "Closing the conversation!" }
                    delay(2000)
                    threadMap.remove(channelId)
                    channel.delete("Thread closed!")
                }

            }
        }

        publicSlashCommand {
            name = "conversation"
            description = "Opens a thread to make a conversation with the AI"

            action {
                val mess = respond {
                    content = "Starting A new conversation! You can talk to me in the attached thread :)"
                }

                val channel: TextChannel = mess.channel.asChannel() as TextChannel

                val thead = channel.startPublicThreadWithMessage(mess.id, "Hello") {
                    name = "AI Conversation"
                    autoArchiveDuration = ArchiveDuration.Hour
                }

                thead.createMessage { content = "Hello! You can start conversing with me here! How can I help you?" }

                val newThread = Conversation(
                    thread_id = thead.id.toString().toLong(),
                    guild_id = thead.guildId.toString().toLong(),
                    prompt = "### Response: Hello! You can start conversing with me here! How can I help you?",
                    created_at = Clock.System.now().toEpochMilliseconds()
                )

                supaClient.postgrest["conversation"].insert(newThread, upsert = true)
            }
        }

        publicSlashCommand(::AskArguments) {
            name = "imagine"
            description = "Generate an image from a prompt"

            action {

                val imageParam: HordeImageParams = HordeImageParams(
                    cfg_scale = 7.0,
                    clip_skip = 1,
                    denoising_strength = 0.75,
                    height = 512,
                    width = 512,
                    hires_fix = false,
                    karras = true,
                    n = 1,
                    post_processing = arrayOf(),
                    sampler_name = "k_euler",
                    seed = "",
                    seed_variation = 1000,
                    steps = 30,
                    tiling = false,
                    facefixer_strength = 0.75
                )

                val imagePayload = HordeImagePayload(
                    censor_nsfw = false,
                    models = arrayOf("Dreamshaper"),
                    nsfw = false,
                    params = imageParam,
                    prompt = arguments.question,
                    r2 = true,
                    shared = false,
                    trusted_workers = false,
                )

                val initialResponse : HttpResponse = httpClient.post("https://stablehorde.net/api/v2/generate/async") {
                    header("apikey", dotenv["AIHORDE"])
                    contentType(ContentType.Application.Json)
                    setBody(imagePayload)
                }

                if (initialResponse.status.value.equals(202)){
                    val responseBody: HordeResponse = initialResponse.body()

                    var statusResponse : HttpResponse = httpClient.get("https://stablehorde.net/api/v2/generate/status/${responseBody.id}")
                    var statusData : HordeImageResponse = statusResponse.body()

                    val mess = respond {
                        content = "Generating Response..."
                    }

                    do {
                        statusResponse = httpClient.get("https://stablehorde.net/api/v2/generate/status/${responseBody.id}")
                        statusData = statusResponse.body()
                        mess.edit { content = "Imagining... Queue Position: ${statusData.queue_position}, ETA: ${formatSeconds(statusData.wait_time)}" }
                        delay(7000)
                    }while (statusData.finished != 1)

                    var responseText = statusData.generations.first().img

                    mess.edit { content = responseText }

                }else{
                    val responseBody: HordeResponse = initialResponse.body()

                    respond {
                        content = "Error! Message:${responseBody.message} \n Json:${responseBody.errors.toString()}"
                    }
                }

            }

        }

        publicSlashCommand(::AskArguments) {
            name = "ask"
            description = "Ask a LLaMa 2 13B model something"

            action {
                val prompt = "\n### Instruction:\n${arguments.question}\n### Response:\n"

                val textParam: HordeTextParams = HordeTextParams(
                    n = 1,
                    max_context_length = 1600,
                    max_length = 140,
                    rep_pen = 1.1,
                    rep_pen_range = 320,
                    rep_pen_slope = 0.7,
                    singleline = false,
                    temperature = 0.7,
                    tfs = 1,
                    top_a = 0,
                    top_k = 100,
                    top_p = 0.92,
                    typical = 1,
                    sampler_order = arrayOf(6, 0, 1, 3, 4, 2, 5),
                    use_default_badwordsids = true,
                    stop_sequence = arrayOf("### Instruction:", "### Response:")
                )

                val payload: HordeTextPayload = HordeTextPayload(
                    prompt = prompt,
                    params = textParam,
                    softprompt = "string",
                    trusted_workers = false,
                    slow_workers = true,
                    workers = arrayOf(),
                    worker_blacklist = false,
                    models = arrayOf("koboldcpp/OpenHermes-2.5-Mistral-7b", "koboldcpp/LLaMA2-13B-Psyfighter2", "koboldcpp/neural-chat-7b-v3-1.Q8_0", "koboldcpp/openchat_3.5-7b", "koboldcpp/Orca-2-13B-q8_0","aphrodite/KoboldAI/LLaMA2-13B-Tiefighter", "KoboldAI/LLaMA2-13B-Tiefighter", "koboldcpp/mistrp-airoboros-7b", "koboldcpp/MythoLogic-Mini-7B"),
                    dry_run = false
                )

                val initialResponse : HttpResponse = httpClient.post("https://stablehorde.net/api/v2/generate/text/async") {
                    header("apikey", dotenv["AIHORDE"])
                    contentType(ContentType.Application.Json)
                    setBody(payload)
                }

                if (initialResponse.status.value.equals(202)){
                    val responseBody: HordeResponse = initialResponse.body()

                    var statusResponse : HttpResponse = httpClient.get("https://stablehorde.net/api/v2/generate/text/status/${responseBody.id}")
                    var statusData : HordeTextResponse = statusResponse.body()

                    val mess = respond {
                        content = "Generating Response..."
                    }

                    do {
                        statusResponse = httpClient.get("https://stablehorde.net/api/v2/generate/text/status/${responseBody.id}")
                        statusData = statusResponse.body()
                        mess.edit { content = "Generating Response... Queue Position: ${statusData.queue_position}" }
                        delay(1000)
                    }while (statusData.finished != 1)

                    var responseText = statusData.generations.first().text.removeSuffix("### Instruction:")
                    responseText = responseText.removeSuffix("### Response:")

                    mess.edit { content = responseText }

                }else{
                    val responseBody: HordeResponse = initialResponse.body()

                    respond {
                        content = "Error! ${responseBody.message}"
                    }
                }

            }

        }

    }

    inner class AskArguments : Arguments() {
        val question by string {
            name = "prompt"
            description = "The prompt you want to give to the ai"
        }
    }

    /**
     * Converts milliseconds to a formatted string in the format "minutes:seconds".
     *
     * @param milliseconds*/
    private fun formatSeconds(seconds: Int): String {
        val minutes = seconds / 60
        val seconds = seconds % 60
        return "${minutes}m:${seconds}s"
    }

}
