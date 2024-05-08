package no.uio.ifi.in2000.andrklae.andrklae.team13.Data.GPT

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class GPTChatResponse(
    val id: String,
    val model: String,
    @SerialName("object") val objectType: String,
    val created: Long,
    val choices: List<Choice>,
    val prompt_filter_results: List<PromptFilterResult>,
    val system_fingerprint: JsonElement? = null, // Using JsonElement to handle any type or null
    val usage: Usage
)

@Serializable
data class Choice(
    val index: Int,
    val finish_reason: String,
    val logprobs: JsonElement? = null, // Handling possible null
    val message: Message,
    val content_filter_results: ContentFilterResults
)

@Serializable
data class ContentFilterResults(
    val hate: FilterDetail,
    val self_harm: FilterDetail,
    val sexual: FilterDetail,
    val violence: FilterDetail
)

@Serializable
data class FilterDetail(
    val filtered: Boolean,
    val severity: String
)

@Serializable
data class PromptFilterResult(
    val prompt_index: Int,
    val content_filter_results: ContentFilterResults
)

@Serializable
data class Usage(
    val completion_tokens: Int,
    val prompt_tokens: Int,
    val total_tokens: Int
)

@Serializable
data class Message(
    val role: String,
    val content: String
)

@Serializable
data class ChatRequest(
    val messages: List<Message>,
    val max_tokens: Int,
    val temperature: Double,
    val frequency_penalty: Double,
    val presence_penalty: Double,
    val top_p: Double,
    val stop: String? = null
)
