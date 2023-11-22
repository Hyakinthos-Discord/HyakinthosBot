package me.thecuddlybear.Bot.extensions

import com.hexadevlabs.gpt4all.LLModel
import com.kotlindiscord.kord.extensions.commands.Arguments
import com.kotlindiscord.kord.extensions.commands.converters.impl.string
import com.kotlindiscord.kord.extensions.extensions.Extension
import com.kotlindiscord.kord.extensions.extensions.publicSlashCommand
import dev.kord.core.behavior.interaction.followup.edit
import dev.kord.core.entity.channel.TextChannel
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.delay
import kotlinx.serialization.Serializable
import me.thecuddlybear.Bot.dotenv
import java.nio.file.Path

@Serializable
data class HordeTextParams(
    val n: Int,
    val max_context_length: Int,
    val max_length: Int,
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

@Serializable
data class HordePayload(
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

@Serializable
data class HordeResponse(
    val id: String = "",
    val kudos: Double = 0.0,
    val message: String = "",
)

@Serializable
data class HordeTextResponseGeneration(
    val text: String,
    val seed: Int,
    val gen_metadata: Array<String>,
    val worker_id: String,
    val worker_name: String,
    val model: String,
    val state: String
)

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

class AiExtension: Extension() {

    override val name: String
        get() = "ai"
    
    val httpClient = HttpClient(CIO) {
        install(ContentNegotiation){
            json()
        }
    }

    override suspend fun setup() {

        publicSlashCommand {
            name = "conversation"
            description = "Opens a thread to make a conversation with the AI"

            action {

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
                    max_length = 120,
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
                
                val payload: HordePayload = HordePayload(
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
                    
                    val mess = respond {
                        content = "Generating Response..."
                    }

                    do {
                        statusResponse = httpClient.get("https://stablehorde.net/api/v2/generate/text/status/${responseBody.id}")
                        statusData = statusResponse.body()
                        mess.edit { content = "Generating Response... Queue Position: ${statusData.queue_position}" }
                        delay(1000)
                    }while (statusData.finished != 1)
                    
                    val responseText = statusData.generations.first().text.removeSuffix("### Instruction:")
                    
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
            name = "question"
            description = "The question you want to ask AI"
        }
    }

}