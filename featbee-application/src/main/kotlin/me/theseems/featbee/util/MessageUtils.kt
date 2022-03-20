package me.theseems.featbee.util

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.entities.ChatId
import com.github.kotlintelegrambot.entities.ParseMode
import org.apache.commons.text.StringSubstitutor

class MessageUtils {
    companion object {
        fun substituteAndSend(substitutionMap: Map<String, Any>, chatId: ChatId, message: String, telegramBot: Bot) {
            val substitution = StringSubstitutor(substitutionMap)
            substitution.setVariablePrefix("{")
            substitution.setVariableSuffix("}")

            val (response, exception) = telegramBot.sendMessage(
                chatId = chatId,
                text = substitution.replace(message),
                parseMode = ParseMode.MARKDOWN
            )
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
