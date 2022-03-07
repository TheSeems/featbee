package me.theseems.featbee.listener

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.entities.ChatId
import me.theseems.featbee.event.FeedbackProducedEvent
import me.theseems.featbee.util.MessageUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.ApplicationListener
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Profile("featbee-telegram")
@Component
class TelegramFeedbackProducedListener : ApplicationListener<FeedbackProducedEvent> {
    @Autowired
    private lateinit var telegramBot: Bot

    @Qualifier("featbee-telegram-receiver")
    @Autowired
    private lateinit var chatId: ChatId

    @Qualifier("featbee-telegram-produced-message")
    @Autowired
    private lateinit var message: String

    override fun onApplicationEvent(event: FeedbackProducedEvent) {
        MessageUtils.substituteAndSend(
            substitutionMap = mapOf(
                "score" to event.feedbackEntity.score,
                "ip" to event.feedbackEntity.ip,
                "content" to (event.feedbackEntity.content ?: "NO COMMENT")
            ),
            telegramBot = telegramBot,
            chatId = chatId,
            message = message
        )
    }
}
