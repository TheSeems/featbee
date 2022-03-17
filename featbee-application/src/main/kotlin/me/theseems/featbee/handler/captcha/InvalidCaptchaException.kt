package me.theseems.featbee.handler.captcha

class InvalidCaptchaException(
    override val message: String? = "Provided captcha is invalid",
    val codes: List<String>? = null
) : RuntimeException()
