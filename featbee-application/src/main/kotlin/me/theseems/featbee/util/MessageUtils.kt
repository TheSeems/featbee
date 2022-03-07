package me.theseems.featbee.util

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.entities.ChatId
import org.apache.commons.text.StringSubstitutor

class MessageUtils {
    companion object {
        fun substituteAndSend(substitutionMap: Map<String, Any>, chatId: ChatId, message: String, telegramBot: Bot) {
            val substitution = StringSubstitutor(substitutionMap)
            substitution.setVariablePrefix("{")
            substitution.setVariableSuffix("}")

            val (response, exception) = telegramBot.sendMessage(chatId, substitution.replace(message))
            if (exception != null) {
                throw RuntimeException(exception)
            }

            if (response == null || !response.isSuccessful) {
                throw RuntimeException(
                    "Failed to send message to telegram:" +
                        " $response (${response?.body()}||${response?.errorBody()})"
                )
            }
        }
    }
}
