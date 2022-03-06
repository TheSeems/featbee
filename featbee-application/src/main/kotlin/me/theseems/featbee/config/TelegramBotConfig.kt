package me.theseems.featbee.config

import com.github.kotlintelegrambot.bot
import com.github.kotlintelegrambot.entities.ChatId
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile

@Profile("featbee-telegram")
@Configuration
class TelegramBotConfig {
    @Value("\${featbee.telegram.token}")
    private lateinit var telegramBotToken: String

    @Value("\${featbee.telegram.receiver}")
    private lateinit var chatId: String

    @Value("\${featbee.telegram.message}")
    private lateinit var message: String

    @Bean
    fun telegramBot() = bot { token = telegramBotToken }

    @Bean
    @Qualifier("featbee-telegram-receiver")
    fun receiverChatId() = ChatId.fromId(chatId.toLong())

    @Bean
    @Qualifier("featbee-telegram-message")
    fun message(): String = message
}
