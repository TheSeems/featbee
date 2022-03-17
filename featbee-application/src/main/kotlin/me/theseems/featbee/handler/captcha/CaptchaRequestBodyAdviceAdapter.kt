package me.theseems.featbee.handler.captcha

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Profile
import org.springframework.core.MethodParameter
import org.springframework.http.HttpInputMessage
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter
import java.io.InputStreamReader
import java.lang.reflect.Type

@Profile("featbee-captcha")
@RestControllerAdvice
class CaptchaRequestBodyAdviceAdapter : RequestBodyAdviceAdapter() {
    companion object {
        const val CAPTCHA_HEADER_NAME = "Featbee-Captcha"
        const val HCAPTCHA_VERIFICATION_URL = "https://www.google.com/recaptcha/api/siteverify"
    }

    @Autowired
    @Qualifier("featbee-captcha-secret")
    private lateinit var captchaSecret: String

    @Autowired
    @Qualifier("featbee-captcha-sitekey")
    private var captchaSitekey: String? = null

    override fun supports(
        methodParameter: MethodParameter,
        targetType: Type,
        converterType: Class<out HttpMessageConverter<*>>
    ) = methodParameter.hasMethodAnnotation(CaptchaRequired::class.java)

    override fun beforeBodyRead(
        inputMessage: HttpInputMessage,
        parameter: MethodParameter,
        targetType: Type,
        converterType: Class<out HttpMessageConverter<*>>
    ): HttpInputMessage {
        val headers = inputMessage.headers
        if (CAPTCHA_HEADER_NAME !in headers || headers[CAPTCHA_HEADER_NAME]?.get(0) == null) {
            throw InvalidCaptchaException()
        }

        val captchaValue = headers[CAPTCHA_HEADER_NAME]!![0]

        val formBuilder = FormBody.Builder()
            .add("response", captchaValue)
            .add("secret", captchaSecret)
        if (captchaSitekey != null) {
            formBuilder.add("sitekey", captchaSitekey!!)
        }

        val request = Request.Builder()
            .url(HCAPTCHA_VERIFICATION_URL)
            .post(formBuilder.build())
            .build()

        val response = OkHttpClient().newBuilder()
            .followRedirects(false).build()
            .newCall(request)
            .execute()

        if (!response.isSuccessful || response.body == null) {
            throw InvalidCaptchaException()
        }

        val responseJson = ObjectMapper().readTree(
            InputStreamReader(response.body!!.byteStream())
                .readText()
        )

        val successField = responseJson["success"]
        if (successField == null || !successField.isBoolean || !successField.asBoolean()) {
            throw InvalidCaptchaException(
                message = "Captcha verification failed",
                codes = responseJson["error-codes"].map(JsonNode::asText)
            )
        }

        return inputMessage
    }
}
