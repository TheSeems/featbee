package me.theseems.featbee.listener

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.entities.ChatId
import me.theseems.featbee.event.FeedbackChangedEvent
import me.theseems.featbee.util.MessageUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.ApplicationListener
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Profile("featbee-telegram")
@Component
class TelegramFeedbackChangedListener : ApplicationListener<FeedbackChangedEvent> {
    @Autowired
    private lateinit var telegramBot: Bot

    @Qualifier("featbee-telegram-receiver")
    @Autowired
    private lateinit var chatId: ChatId

    @Qualifier("featbee-telegram-changed-message")
    @Autowired
    private lateinit var message: String

    override fun onApplicationEvent(event: FeedbackChangedEvent) {
        MessageUtils.substituteAndSend(
            substitutionMap = mapOf(
                "ip" to event.feedbackEntity.ip,
                "newScore" to event.feedbackEntity.score,
                "oldScore" to event.previousFeedbackEntity.score,
                "newVisibility" to event.feedbackEntity.visibility,
                "oldVisibility" to event.previousFeedbackEntity.visibility,
                "newContent" to (event.feedbackEntity.content ?: "NO COMMENT"),
                "oldContent" to (event.previousFeedbackEntity.content ?: "NO COMMENT")
            ),
            telegramBot = telegramBot,
            chatId = chatId,
            message = message
        )
    }
}
