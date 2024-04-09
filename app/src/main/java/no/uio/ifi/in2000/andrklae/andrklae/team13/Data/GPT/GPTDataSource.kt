package no.uio.ifi.in2000.andrklae.andrklae.team13.Data.GPT

import com.aallam.openai.api.BetaOpenAI
import com.aallam.openai.api.chat.ChatCompletion
import com.aallam.openai.api.chat.ChatCompletionRequest
import com.aallam.openai.api.chat.ChatMessage
import com.aallam.openai.api.chat.ChatRole
import com.aallam.openai.api.model.ModelId
import com.aallam.openai.client.OpenAI


class GPTDataSource {
    @OptIn(BetaOpenAI::class)
    suspend fun getGPTResponse(prompt: String): String{
        try {
            val apiKey = OpenAI("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")
            val chatCompletionRequest = ChatCompletionRequest(
                model = ModelId("gpt-3.5-turbo"),
                messages = listOf(
                    ChatMessage(
                        role = ChatRole.User,
                        content = prompt
                    )
                )
            )
            val completion: ChatCompletion = apiKey.chatCompletion(chatCompletionRequest)
            return completion.choices.first().message?.content.toString()
        } catch (e: Exception) {
            return "failed"
        }

    }
}
