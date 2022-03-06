package me.theseems.featbee.listener

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.entities.ChatId
import me.theseems.featbee.event.FeedbackProduceEvent
import org.apache.commons.text.StringSubstitutor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.ApplicationListener
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Profile("featbee-telegram")
@Component
class TelegramProduceFeedbackListener : ApplicationListener<FeedbackProduceEvent> {
    @Autowired
    private lateinit var telegramBot: Bot

    @Qualifier("featbee-telegram-receiver")
    @Autowired
    private lateinit var chatId: ChatId

    @Qualifier("featbee-telegram-message")
    @Autowired
    private lateinit var message: String

    override fun onApplicationEvent(event: FeedbackProduceEvent) {
        val substitution = StringSubstitutor(
            mapOf(
                "score" to event.feedbackEntity.score,
                "ip" to event.feedbackEntity.ip,
                "content" to (event.feedbackEntity.content ?: "NO COMMENT")
            )
        )

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
