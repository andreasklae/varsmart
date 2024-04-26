package no.uio.ifi.in2000.andrklae.andrklae.team13.Data.GPT

import com.azure.ai.openai.OpenAIClient;
import com.azure.ai.openai.OpenAIClientBuilder;
import com.azure.ai.openai.models.Choice;
import com.azure.ai.openai.models.Completions;
import com.azure.ai.openai.models.CompletionsOptions;
import com.azure.ai.openai.models.CompletionsUsage;
import com.azure.core.credential.AzureKeyCredential;
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.client.statement.readText
import io.ktor.serialization.kotlinx.json.json
import io.ktor.util.InternalAPI
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


class GPTDataSource {
    private val json = Json {
        ignoreUnknownKeys = true
    }

    @OptIn(InternalAPI::class)
    // function for generating ai response to a prompt
    suspend fun getGPTResponse(prompt: String): String {
        val client = HttpClient(CIO) {
            install(ContentNegotiation) {
                json(this@GPTDataSource.json)
            }
        }
        // custom azure model
        val deploymentId = "MrPraktisk"
        val baseUrl =
            "https://gpt-ifi-in2000-swe-1.openai.azure.com" +
                    "/openai/deployments/$deploymentId/chat/completions" +
                    "?api-version=2024-02-15-preview"
        val key = "06e1dffe13824c2a9c110d462397bdf3"
        try {
            client.use { httpClient ->
                // variables for the api
                val request = ChatRequest(
                    messages = listOf(Message("user", prompt)),
                    max_tokens = 4000,
                    temperature = 0.7,
                    frequency_penalty = 0.0,
                    presence_penalty = 0.0,
                    top_p = 0.95,
                    stop = null
                )

                // fetches from api
                val response: HttpResponse = httpClient.post(baseUrl) {
                    header("Content-Type", "application/json")
                    header("api-key", key)
                    body = json.encodeToString(request)
                }

                println(response)

                // serializes the Json response
                val responseBody = response.bodyAsText()
                println("Gpt response: $responseBody")
                val gptResponse = json.decodeFromString<GPTChatResponse>(responseBody)

                // returns the response from the ai
                return gptResponse.choices.first().message.content.trim()
            }
        }
        // failed getting response from api
        catch (e: Exception) {
            return "Kunne ikke generere tekst. Er du koblet til internett?"
        }
    }
}