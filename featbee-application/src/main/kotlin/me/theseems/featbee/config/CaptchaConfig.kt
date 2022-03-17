package me.theseems.featbee.config

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile

@Profile("featbee-captcha")
@Configuration
class CaptchaConfig {
    @Value("\${featbee.captcha.secret}")
    private lateinit var secret: String

    @Value("\${featbee.captcha.sitekey}")
    private var sitekey: String? = null

    @Bean
    @Qualifier("featbee-captcha-secret")
    fun secret() = secret

    @Bean
    @Qualifier("featbee-captcha-sitekey")
    fun sitekey() = sitekey
}
