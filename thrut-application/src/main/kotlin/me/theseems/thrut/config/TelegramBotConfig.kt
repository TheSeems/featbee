package me.theseems.thrut.config

import com.github.kotlintelegrambot.bot
import com.github.kotlintelegrambot.entities.ChatId
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile

@Profile("thrut-telegram")
@Configuration
class TelegramBotConfig {
    @Value("\${thrut.telegram.token}")
    private lateinit var telegramBotToken: String

    @Value("\${thrut.telegram.receiver}")
    private lateinit var chatId: String

    @Value("\${thrut.telegram.message}")
    private lateinit var message: String

    @Bean
    fun telegramBot() = bot { token = telegramBotToken }

    @Bean
    @Qualifier("thrut-telegram-receiver")
    fun receiverChatId() = ChatId.fromId(chatId.toLong())

    @Bean
    @Qualifier("thrut-telegram-message")
    fun message(): String = message
}
